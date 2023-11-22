package com.example.petme;

public class FriendRequest {

    private String requestId;
    private String senderUserId;
    private String receiverUserId;
    private String status;

    public FriendRequest() { } // Firebase 에서 데이터를 읽어올때 필요.

    public FriendRequest(String requestID, String senderUserId, String receiverUserId, String status){
        this.requestId = requestID;
        this.senderUserId = senderUserId;
        this.receiverUserId = receiverUserId;
        this.status = status;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public String getReceiverUserId() {
        return receiverUserId;
    }

    public String getStatus() {
        return status;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public void setReceiverUserId(String receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
