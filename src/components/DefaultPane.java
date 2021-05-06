package components;

public class DefaultPane extends CustomPane {

	public DefaultPane() {
		super();
	}

	@Override
	public void definePaneParams(double height, double width) {
		// TODO Auto-generated method stub
		this.setMinWidth(width);
		this.setMaxWidth(width);
		this.setMinHeight(height);
		this.setMaxHeight(height);
		
	}
}
