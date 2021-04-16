package com.adrianghub.zombie;

public enum WeaponType {
    PISTOL,
    SHOTGUN,
    TRIPLE_SHOTGUN;

    public static WeaponType upgradeWeaponByScore(int score) {
        if (score > 8000) return SHOTGUN;

        return PISTOL;
    }
}
