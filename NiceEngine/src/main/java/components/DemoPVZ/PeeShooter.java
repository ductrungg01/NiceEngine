package components.DemoPVZ;

import components.Component;
import components.StateMachine;

public class PeeShooter extends Component {
    private transient StateMachine stateMachine;

    @Override
    public void start() {
//        stateMachine = this.gameObject.getComponent(StateMachine.class);
//        stateMachine.setCurrentState("Shoot");
    }
}
