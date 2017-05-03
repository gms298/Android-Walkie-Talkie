package com.example.nam_o.walkietalkie;

/**
 * https://code.tutsplus.com/tutorials/create-a-bluetooth-scanner-with-androids-bluetooth-api--cms-24084
 */
public class DeviceInfo
{
    private String name;
    private String address;

    public DeviceInfo(String name, String address)
    {
        this.name = name;
        this.address = address;
    }
    // Return Device name
    public String getName()
    {
        return name;
    }
    // Return Device address
    public String getAddress()
    {
        return address;
    }

    @Override
    public String toString() {
        return getName();
    }
}
