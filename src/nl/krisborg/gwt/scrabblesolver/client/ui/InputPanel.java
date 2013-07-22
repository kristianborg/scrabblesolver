package nl.krisborg.gwt.scrabblesolver.client.ui;

import nl.krisborg.gwt.scrabblesolver.client.ui.interfaces.AddWordListener;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.DomEvent.Type;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class InputPanel extends PopupPanel {
	
	private VerticalPanel verticalPanel = new VerticalPanel();
	private Label title;
	private TextBox input;
	private HorizontalPanel buttons;
	private Button okButton;
	private Button cancelButton;
	private AddWordListener listener;
	
	private int x;
	private int y;
	
	public InputPanel(AddWordListener listener){
		this.listener = listener;
		
		title = new Label("Vul woord in");
		verticalPanel.add(title);
		
		input = getTextBox();
		verticalPanel.add(input);
		
		buttons = new HorizontalPanel();
		okButton = getOKButton();
		buttons.add(okButton);
		
		cancelButton = getCancelButton();
		buttons.add(cancelButton);
		
		verticalPanel.add(buttons);
		
		setWidget(verticalPanel);
	}
	
	public void setMaxInputLength(int maxLength){
		input.setMaxLength(maxLength);
	}
	
	public void setLocation(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void show() {
		super.show();
		input.setFocus(true);
	}
	
	@Override
	protected void onPreviewNativeEvent(NativePreviewEvent event) {
		super.onPreviewNativeEvent(event);
        if (event.getTypeInt() ==  Event.ONKEYUP){
        	int keyCode = event.getNativeEvent().getKeyCode();
            if (keyCode == KeyCodes.KEY_ESCAPE) {
                doCancel();
            } else if (keyCode == KeyCodes.KEY_ENTER){
            	doOk();
            }
        }
	}
	
	private TextBox getTextBox(){
		TextBox result = new TextBox();
		result.addKeyPressHandler(new KeyPressHandler() {
		      public void onKeyPress(KeyPressEvent event) {
		    	  char keyCode = event.getCharCode();
		    	  if (Character.isLetterOrDigit(keyCode)){
		    		  listener.addTempWord(x, y, input.getText() + (char)keyCode);
		    	  } else {
		    		  // TODO: Error?
		    	  }		    	  
		      }
		});
		      
		return result;
		
	}
	
	private Button getCancelButton(){
		Button button = new Button("Cancel");
		button.addClickHandler(new ClickHandler(){
			
			@Override
			public void onClick(ClickEvent event) {
				doCancel();
			}
		});
		return button;
	}
	
	private Button getOKButton(){
		Button button = new Button("OK");
		button.addClickHandler(new ClickHandler(){
			
			@Override
			public void onClick(ClickEvent event) {
				doOk();
			}
		});
		return button;
	}
	
	private void doOk(){
		listener.addWord(x, y, input.getText());
		input.setText("");
		setGlassEnabled(false);
		hide();
	}
	
	private void doCancel(){
		listener.clearTempWord();
		input.setText("");
		setGlassEnabled(false);
		hide();
	}
}
