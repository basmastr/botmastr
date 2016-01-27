package botmastr;

/**
 * Needs to be implemented by all classes which want to ask for resources (minerals and/or gas) allocation.
 * @author Tomas Tomek tomas.tomek333@gmail.com
 */
public interface IResourcesRequestor {
    /**
     * Informs the requestor that the request has been accepted and it should be executed.
     * @param request Request that has been accepted.
     */
    void requestAccepted(ResourcesRequest request);

    /**
     * Informs the requestor that the request has been denied and it should deal with it.
     * @param request Request that has been denied.
     */
    void requestDenied(ResourcesRequest request);
}
