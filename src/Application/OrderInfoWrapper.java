package Application;

import java.time.LocalDate;

/*
Class to store info about an order. To be populated when loading an order
 */
public class OrderInfoWrapper {
    private String orderID;
    private String clientName;
    private LocalDate dueDate;
    private LocalDate lastModified;
    private String orderStatus;

    public OrderInfoWrapper(String orderID, String clientName, LocalDate dueDate, LocalDate lastModified, String orderStatus){
        this.orderID = orderID;
        this.clientName = clientName;
        this.dueDate = dueDate;
        this.lastModified = lastModified;
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
