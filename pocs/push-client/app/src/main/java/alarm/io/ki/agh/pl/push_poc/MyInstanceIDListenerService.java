package alarm.io.ki.agh.pl.push_poc;

import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;

public class MyInstanceIDListenerService extends InstanceIDListenerService {

    public MyInstanceIDListenerService() {
        super();
        String iid = InstanceID.getInstance(this).getId();

    }

    @Override
    public void onTokenRefresh() {
        refreshAllTokens();
    }

    private void refreshAllTokens() {
        // assuming you have defined TokenList as
        // some generalized store for your tokens
        InstanceID iid = InstanceID.getInstance(this);
        // TODO: refresh
    }

}
