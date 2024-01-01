package net.teranity.api.reward;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Reward {
    @NonNull private String rewardName;
    @NonNull private RewardType rewardType;
    @NonNull private int max;
}
