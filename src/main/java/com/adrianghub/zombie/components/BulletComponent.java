package com.adrianghub.zombie.components;

import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.component.Component;

public class BulletComponent extends Component {

    @Override
    public void onAdded() {
        entity.getComponent(ProjectileComponent.class).getVelocity();
    }
}
