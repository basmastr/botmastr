package java.botmastr;

import bwapi.UnitType;
import bwapi.UpgradeType;

/**
 * Represents a single request for resources.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public class ResourcesRequest  implements Comparable<ResourcesRequest>{
    /**
     * Amount of minerals requested.
     */
    protected Integer minerals;

    /**
     * Amount of gas requested.
     */
    protected Integer gas;

    /**
     * Priority of the request. Reqest with higher priority will get accepted sooner in case of multiple requests.
     */
    protected EPriority priority;

    /**
     * Reference to object which initiated this request.
     */
    protected IResourcesRequestor requestor;

    /**
     * What are the resources for.
     */
    protected UnitType|UpgradeType reason;

    /**
     * Construtor for requests with known priority.
     * @param minerals Amount of minerals requested.
     * @param gas Amount of gas requested.
     * @param priority Priority of the request.
     */
    public ResourcesRequest(Integer minerals, Integer gas, EPriority priority) {
        this.minerals = minerals;
        this.gas = gas;
        this.priority = priority;
    }

    /**
     * Construtor for requests with unknown priority. Priority will be set to medium.
     * @param minerals Amount of minerals requested.
     * @param gas Amount of gas requested.
     */
    public ResourcesRequest(Integer minerals, Integer gas) {
        this.minerals = minerals;
        this.gas = gas;
        this.priority = EPriority.MEDIUM;
    }

    /**
     * Sends the request to the ResourceManager.
     */
    public void send() {
        ResourceManager.getInstance().ask(this);
    }


    /**
     * Accepts the request, signalling the requestor to act on it.
     */
    public void accept() {
        this.requestor.requestAccepted(this);
    }

    /**
     * Deny the request, signalling the requestor to deal with it.
     */
    public void deny() {
        this.requestor.requestDenied(this);
    }

    public Integer getMinerals() {
        return this.minerals;
    }

    public Integer getGas() {
        return this.gas;
    }

    public EPriority getPriority() {
        return this.priority;
    }

    public IResourcesRequestor getRequestor() {
        return this.requestor;
    }

    @Override
    public int compareTo(ResourcesRequest o) {
        return this.priority.compareTo(o.getPriority());
    }
}
