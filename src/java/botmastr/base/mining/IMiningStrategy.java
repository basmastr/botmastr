package botmastr.base.mining;

import botmastr.unit.UnitData;
import bwapi.Unit;

import java.util.List;
import java.util.Set;

/**
 * Interface for classes implementing resources mining strategy.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public interface IMiningStrategy {


    void tic(Set<UnitData> workers, Set<Unit> minerals, Set<Unit> geysers);
}
