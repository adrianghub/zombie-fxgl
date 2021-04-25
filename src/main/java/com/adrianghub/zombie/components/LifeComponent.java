package com.adrianghub.zombie.components;

import com.adrianghub.zombie.ZombieApp;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

public class LifeComponent extends Component {
    @Override
    public void onUpdate(double tpf) {
        followPlayer(tpf);
    }

    private void followPlayer(double tpf) {
        Entity player = getEntity().getWorld().getSingleton(ZombieApp.EntityType.SURVIVOR);
        if (getEntity().distance(player) < 150) {
            getEntity().translateTowards(player.getCenter(), 150 * tpf);
        }
    }
}
