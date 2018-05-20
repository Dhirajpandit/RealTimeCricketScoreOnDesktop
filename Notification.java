import java.util.List;

import com.notification.NotificationFactory;
import com.notification.NotificationFactory.Location;
import com.notification.NotificationManager;
import com.notification.manager.SimpleManager;
import com.notification.types.TextNotification;
import com.theme.ThemePackagePresets;
import com.utils.Time;
/**
 * 
 * @author Dhiraj Pandit
 * @since 19-May-2018
 * notification panel
 */
public class Notification {
	public static void showOnDesktop(List<String> matchInfo) throws InterruptedException {
		// makes a factory with the built-in clean theme
		NotificationFactory factory = new NotificationFactory(ThemePackagePresets.cleanDark());
		// a normal manager that just pops up the notification
		NotificationManager plain = new SimpleManager(Location.WEST);
		// creates a text notification
		StringBuilder text = new StringBuilder();
		for(int i=1;i<matchInfo.size();i++) {
			text.append(matchInfo.get(i)).append("\n");
		}
		TextNotification notification = factory.buildTextNotification(matchInfo.get(0),
				text.toString());
		//clear notification panel if already there. 
		if(plain.getNotifications().size()>0 || plain.getNotifications()!=null) {
        	plain.removeNotification(notification);
        }
		//close when click on notification panel
		notification.setCloseOnClick(true);
		notification.setSize(400, 300);
		notification.setOpacity(60.0);
		//add here actual Notification widget 
		plain.addNotification(notification, Time.infinite());
        
	}
}
