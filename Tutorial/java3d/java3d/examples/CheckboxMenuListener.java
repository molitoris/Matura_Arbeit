//
//  INTERFACE
//    CheckboxMenuListener  -  listen for checkbox change events
//
//  DESCRIPTION
//    The checkboxChanged method is called by users of this class
//    to notify the listener when a checkbox choice has changed on
//    a CheckboxMenu class menu.
//

import java.util.EventListener;

interface CheckboxMenuListener
	extends EventListener
{
	public void checkboxChanged( CheckboxMenu menu, int check );
}
