package net.teranity.api.reward;

public enum RewardType {
    LEVEL(0),
    ACHIEVEMENTS(1),
    GAME(2),
    EVENTS(3),
    CUSTOM(4);

    private int id;

    RewardType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static RewardType getTypeByID(int id) {
        for (RewardType rewardType : values()) {
            if (rewardType.getId() == id) {
                return  rewardType;
            }
        } return null;
    }
}
