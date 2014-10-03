package com.dsi11.teleportstations.database;

import jdk.nashorn.internal.ir.Block;
import net.minecraft.util.BlockPos;

/**
 * TeleData is a dataset storing all required data for one teleporter.
 * <p/>
 * Used by {@link Database} for internal saving.
 *
 * @author Adanaran
 */
public class TeleData {

    private BlockPos position;
    private String name;
    private BlockPos target;
    private int meta, worldType;

    /**
     * Constructor for a new TeleData object without target.
     * <p/>
     * Stores initial values.
     *
     * @param name      String name of teleporter
     * @param i         int x-coordinate
     * @param j         int y-coordinate
     * @param k         int z-coordinate
     * @param meta      int meta-type of teleporter
     * @param worldType int normal or nether world?
     */
    public TeleData(String name, int i, int j, int k, int meta, int worldType) {
        this.position = new BlockPos(i, j, k);
        this.name = name;
        this.target = null;
        this.meta = meta;
        this.worldType = worldType;
    }

    /**
     * Constructor for a new TeleData object with target.
     * <p/>
     * Stores initial values.
     *
     * @param name      String name of teleporter
     * @param i         int x-coordinate
     * @param j         int y-coordinate
     * @param k         int z-coordinate
     * @param meta      int meta-type of teleporter
     * @param worldType int normal or nether world?
     * @param ziel      {@link BlockPos} of the telporter's target
     */
    public TeleData(String name, int i, int j, int k, int meta, int worldType,
                    BlockPos ziel) {
        this.position = new BlockPos(i, j, k);
        this.name = name;
        this.target = ziel;
        this.meta = meta;
        this.worldType = worldType;
    }

    /**
     * Constructor for a new TeleData object converted from a String.
     * <p/>
     * Stores initial values.
     *
     * @param line String the TeleData.toString of this new teleporter
     */
    public TeleData(String line) {
        String[] tdString = line.split(";;");
        try {
            this.name = tdString[0];

            int posX = Integer.parseInt(tdString[1]);
            int posY = Integer.parseInt(tdString[2]);
            int posZ = Integer.parseInt(tdString[3]);
            this.position = new BlockPos(posX, posY, posZ);

            this.meta = Integer.parseInt(tdString[4]);
            this.worldType = Integer.parseInt(tdString[5]);
            if (tdString.length == 9) {
                this.target = new BlockPos(Integer.parseInt(tdString[6]),
                        Integer.parseInt(tdString[7]),
                        Integer.parseInt(tdString[8]));
            } else if (tdString.length == 7) {
                this.target = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the teleporter's target.
     *
     * @return {@link BlockPos} current target's coordinates
     */
    public BlockPos getTarget() {
        return target;
    }

    /**
     * Sets the teleporter's target.
     *
     * @param target {@link BlockPos} target coordinates
     * @return
     */
    public TeleData setTarget(BlockPos target) {
        this.target = target;
        return this;
    }

    /**
     * Gets the teleporters meta value.
     *
     * @return int meta-type of teleporter.
     */
    public int getMeta() {
        return meta;
    }

    /**
     * Sets the teleporter's meta value.
     *
     * @param meta int teleporter's new meta
     */
    public TeleData setMeta(int meta) {
        this.meta = meta;
        return this;
    }

    /**
     * Gets the teleporter's name.
     *
     * @return String name of this teleporter
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this teleporter.
     *
     * @param name String name of the teleporter
     * @return
     */
    public TeleData setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets the world type of this teleporter.
     *
     * @return int 0 normal world, -1 nether worlds
     */
    public int getWorldType() {
        return worldType;
    }

    public BlockPos getPosition(){
        return this.position;
    }

    public int getX() {
        return this.position.getX();
    }

    public int getY() {
        return this.position.getY();
    }

    public int getZ() {
        return this.position.getZ();
    }

    /**
     * Converts this TeleData object into a String.
     * <p/>
     * Overriding object's toString()-method.
     *
     * @return String name$$i$$j$$k$$meta$$null oder
     * name$$i$$j$$k$$meta$$target.i$$target.j$$target.k
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name).append(";;").append(position.getX()).append(";;")
                .append(position.getY()).append(";;").append(position.getZ()).append(";;")
                .append(meta).append(";;").append(worldType).append(";;");
        if (target != null) {
            builder.append(target.getX()).append(";;").append(target.getY())
                    .append(";;").append(target.getZ());
        } else {
            builder.append("null");
        }
        return builder.toString();
    }
}
