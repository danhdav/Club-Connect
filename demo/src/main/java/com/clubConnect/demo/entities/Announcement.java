package com.clubConnect.demo.entities;

public class Announcement
{
    //vars
    private String message;
    private Long id;
    private Long timestamp;
    private Long clubId; //club it was posted in
    private Long userId; //person who posted it

    //get methods
    public String getMessage() { return message; }
    public Long getId() { return id; }
    public Long getClubId() { return clubId; }
    public Long getUserId() { return userId; }
    public Long getTimestamp() { return timestamp; }

    //set methods
    public void setMessage(String newMessage)
    {
        message = newMessage;
    }
    public void setId(Long newId)
    {
        id = newId;
    }
    public void setTimestamp(Long newTimestamp)
    {
        timestamp = newTimestamp;
    }
    public void setClubId(Long newClubId)
    {
        clubId = newClubId;
    }
    public void setUserId(Long newUserId)
    {
        userId = newUserId;
    }


}
