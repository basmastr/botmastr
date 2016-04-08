package botmastr.navigation;

import botmastr.common.AManager;
import botmastr.common.Common;
import botmastr.unit.Squad;
import botmastr.unit.UnitData;
import botmastr.unit.UnitManager;
import bwapi.*;
import bwta.Chokepoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages navigation of units and squads.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public final class NavigationManager extends AManager {
    /**
     * Singleton intance.
     */
    private static final NavigationManager INSTANCE = new NavigationManager();

    /**
     * All in walktiles (8px)
     */
    private int checkRange;
    private int mapW;
    private int mapH;

    /**
     * Private constructor cos this is a singleton.
     */
    private NavigationManager() {
    }

    public static NavigationManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void init(Mirror bwapi) {
        super.init(bwapi);
        this.checkRange = 5;
        this.mapW = this.bwapi.getGame().mapWidth() * 4;
        this.mapH = this.bwapi.getGame().mapHeight() * 4;
    }

    @Override
    public void tic() {
        if (Common.getInstance().debug()) {
            debug();
        }
    }

    private void debug() {
        final List<Unit> selected = this.bwapi.getGame().getSelectedUnits();
        if (!selected.isEmpty()) {
            selected.forEach(u -> displayPF(UnitManager.getInstance().getUnitById(u.getID())));
        }
    }

    /** Is used to compute and execute movement commands for attacking units using the potential field
     * navigation system. */
    public final boolean computeMove(UnitData agent)
//    , TilePosition goal
    {
        if (!agent.getUnit().canMove()) {
            return false;
        }

        boolean cmd = false;
        int radius = agent.getUnit().getType().seekRange();
        if (agent.getUnit().getType().sightRange() > radius)
        {
            radius = agent.getUnit().getType().sightRange();
        }

        //Retreat to center of the squad if the enemy
        //is overwhelming.
//        if (agent.getUnit().isUnderAttack() && (agent.getUnit().isIdle() || agent.getUnit().isInterruptible()))
//        {
//            int ownI = MapManager.getInstance().getOwnGroundInfluenceIn(agent.getUnit().getTilePosition());
//            int enI = MapManager.getInstance().getEnemyGroundInfluenceIn(agent.getUnit().getTilePosition());
//            if (enI > ownI)
//            {
//                //this.bwapi.getGame() << "Retreat from (" << agent->getUnit()->getTilePosition().x << "," << agent->getUnit()->getTilePosition().y << " " << ownI << "<" << enI << endl;
//                Squad sq = Commander.getInstance().getSquad(agent.getSquadID());
//                if (sq != null && !sq.isExplorer())
//                {
//                    TilePosition center = sq.getCenter();
//                    if (center.getDistance(agent.getUnit().getTilePosition()) >= 4 && !agent.getUnit().isCloaked())
//                    {
//                        if (agent.getUnit().isSieged())
//                        {
//                            agent.getUnit().unsiege();
//                            return true;
//                        }
//                        if (agent.getUnit().isBurrowed())
//                        {
//                            agent.getUnit().unburrow();
//                            return true;
//                        }
//                        agent.getUnit().rightClick(Position(center.getX(), center.getY()));
//                        return true;
//                    }
//                }
//            }
//        }

        if (enemyInRange(agent, radius))
        {
//            if (pathfinding_version == 0)
//            {
//                Profiler.getInstance().start("NormMove");
//                cmd = computePathfindingMove(agent, goal);
//                Profiler.getInstance().end("NormMove");
//            }
//
//            if (pathfinding_version == 1)
//            {
//                Profiler.getInstance().start("BoidsMove");
//                cmd = computeBoidsMove(agent);
//                Profiler.getInstance().end("BoidsMove");
//            }

//            if (pathfinding_version == 2)
//            {
//                Profiler.getInstance().start("PFmove");
                computePotentialFieldMove(agent);
//                Profiler.getInstance().end("PFmove");
//            }
        }
//        else
//        {
//            Profiler.getInstance().start("NormMove");
//            cmd = computePathfindingMove(agent, goal);
//            Profiler.getInstance().end("NormMove");
//        }
        return cmd;
    }

    /**
     *
     * @param unit
     * @param range
     * @return {@code true} if there is at least one enemy unit in {@code range} radius around the {@code unit}, {@code false} otherwise.
     */
    protected boolean enemyInRange(UnitData unit, int range) {
        return Common.getInstance().getEenemy().getUnits().stream()
                .anyMatch(u -> unit.getUnit().getDistance(u) <= range);
    }

    /** Computes a PF move (enemy units within range) */
    public final boolean computePotentialFieldMove(UnitData agent)
    {
        if (!agent.getUnit().isIdle() && !agent.getUnit().isMoving())
        {
            return false;
        }

        Unit unit = agent.getUnit();

        if (unit.isSieged() || unit.isBurrowed() || unit.isLoaded())
        {
            return false;
        }

        WalkPosition unitWT = new WalkPosition(unit.getPosition().getX()/8, unit.getPosition().getY()/8);
        int wtX = unitWT.getX();
        int wtY = unitWT.getY();

        float bestP = getAttackingUnitP(agent, unitWT);
        //bestP += PFFunctions::getGoalP(Position(unitX,unitY), goal);
        //bestP += PFFunctions::getTrailP(agent, unitX, unitY);
        bestP += PFFunctions.getTerrainP(agent, unitWT);

        float cP = 0F;

        float startP = bestP;
        int bestX = wtX;
        int bestY = wtY;

        for (int cX = wtX - checkRange; cX <= wtX + checkRange; cX++)
        {
            for (int cY = wtY - checkRange; cY <= wtY + checkRange; cY++)
            {
                if (cX >= 0 && cY >= 0 && cX <= mapW && cY <= mapH)
                {
                    WalkPosition wt = new WalkPosition(cX, cY);
                    cP = getAttackingUnitP(agent, wt);
                    //cP += PFFunctions::getGoalP(Position(cX,cY), goal);
                    //cP += PFFunctions::getTrailP(agent, cX, cY);
                    cP += PFFunctions.getTerrainP(agent, wt);

                    if (cP > bestP)
                    {
                        bestP = cP;
                        bestX = cX;
                        bestY = cY;
                    }
                }
            }
        }

        if (bestX != wtX || bestY != wtY)
        {
            WalkPosition wt = new WalkPosition(bestX, bestY);
            Position toMove = new Position(wt.getX()*8, wt.getY()*8);

            return agent.getUnit().attack(toMove);
        }

        return false;
    }

    /** Calculates the potential field values for an attacking unit. */
    private float getAttackingUnitP(UnitData agent, WalkPosition wp)
    {
        float p = 0F;

        //Enemy Units
        for (Unit u : this.bwapi.getGame().enemy().getUnits())
        {
            //Enemy seen

            UnitType t = u.getType();
            boolean retreat = false;
            if (!agent.getUnit().getType().canAttack() && agent.getUnit().getType().isFlyer())
            {
                retreat = true;
            }
            if (!agent.getUnit().getType().canAttack() && !agent.getUnit().getType().isFlyer())
            {
                retreat = true;
            }
            if (agent.getUnit().getGroundWeaponCooldown() >= 20 || agent.getUnit().getAirWeaponCooldown() >= 20)
            {
                retreat = true;
            }

            float dist = PFFunctions.getDistance(wp, u);
            if (!retreat)
            {
                p += PFFunctions.calcOffensiveUnitP(dist, agent.getUnit(), u);
            }
            if (retreat)
            {
                p += PFFunctions.calcDefensiveUnitP(dist, agent.getUnit(), u);
            }
        }

        //Own Units
        for (UnitData a : UnitManager.getInstance().getUnits().values())
        {
            if (a.getUnit().exists())
            {
                float dist = PFFunctions.getDistance(wp, a.getUnit());
                p += PFFunctions.calcOwnUnitP(dist, wp, agent.getUnit(), a.getUnit());
            }
        }

        //Neutral Units
        for (Unit u : this.bwapi.getGame().getNeutralUnits())
        {
            if (u.getType().equals(UnitType.Terran_Vulture_Spider_Mine))
            {
                WalkPosition w2 = new WalkPosition(u.getPosition().getX()/8, u.getPosition().getY()/8);
                float dist = PFFunctions.getDistance(wp, u);
                if (dist <= 2)
                {
                    p -= 50.0;
                }
            }
        }

        return p;
    }

    /** Displays a debug view of the potential fields for an agent. */
    public void displayPF(UnitData agent)
    {
        if (agent == null || !agent.getUnit().canMove()) {
            return;
        }
        Unit unit = agent.getUnit();
        if (unit.isBeingConstructed())
            return;

        //PF
        WalkPosition w = new WalkPosition(agent.getUnit().getX()/8, agent.getUnit().getY()/8);
        if (!w.isValid()) {
            System.out.println("Invalid walkposition");
        }
        int tileX = w.getX();
        int tileY = w.getY();
        int range = 10 * 3;

        for (int cTileX = tileX - range; cTileX < tileX + range; cTileX += 3)
        {
            for (int cTileY = tileY - range; cTileY < tileY + range; cTileY += 3)
            {
                if (cTileX >= 0 && cTileY >= 0 && cTileX < mapW && cTileY < mapH)
                {
                    WalkPosition wt = new WalkPosition(cTileX + 1, cTileY + 1);
                    float p = getAttackingUnitP(agent, wt);
                    //cP += PFFunctions::getGoalP(Position(cX,cY), goal);
                    //cP += PFFunctions::getTrailP(agent, cX, cY);
                    p += PFFunctions.getTerrainP(agent, wt);

                    //print box
                    if (p > -950)
                    {
                        Position pos = new Position(wt.getX()*8, wt.getY()*8);
                        this.bwapi.getGame().drawBoxMap(pos.getX() - 8, pos.getY() - 8, pos.getX() + 8, pos.getY() + 8, getColor(p), true);
                    }
                }
            }
        }
    }

    private Color getColor(float p)
    {
        if (p >= 0)
        {
            int v = (int)(p * 3);
            int halfV = (int)(p * 0.6);

            if (v > 255)
            {
                v = 255;
            }
            if (halfV > 255)
            {
                halfV = 255;
            }

            return new Color(halfV, halfV, v);
        }
        else
        {
            p = -p;
            int v = (int)(p * 1.6);

            int v1 = 255 - v;
            if (v1 <= 0)
            {
                v1 = 40;
            }
            int halfV1 = (int)(v1 * 0.6);

            return new Color(v1, halfV1, halfV1);
        }
    }
}
