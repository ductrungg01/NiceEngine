package components.mariodemo;

import components.Component;
import components.StateMachine;

import java.util.ArrayList;
import java.util.List;

public class ItemBox extends Component {
    private float changeStateTime = 0.08f;
    private float stateTime = changeStateTime;
    protected transient StateMachine stateMachine;
    protected transient int currentState;
    private List<String> listState = new ArrayList<>();

    @Override
    public void start() {
        stateMachine = this.gameObject.getComponent(StateMachine.class);
        this.listState.add("Mushroom");
        this.listState.add("Flower");
        this.listState.add("Star");
        currentState = listState.size() - 1;
    }

    @Override
    public void update(float dt) {
        if (stateTime > 0) {
            stateTime -= dt;
        } else {
            stateTime = changeStateTime;
            if (currentState < listState.size() - 1) {
                currentState++;
            } else currentState = 0;
        }
        stateMachine.setCurrentState(listState.get(currentState));
    }
}
