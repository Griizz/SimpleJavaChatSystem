import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UserDataBank implements Runnable
{
    private HashMap<String, String> dictonary;
    private List<String> blacklist;

    public UserDataBank()
    {
        blacklist = new ArrayList<>();
        dictonary = new HashMap<>();
        dictonary.put("Hans", "1234");
        dictonary.put("Peter", "P@ssw0rt");
        dictonary.put("Helga", "helga666");

        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this, 0, 1, TimeUnit.SECONDS);
    }

    public boolean CheckLoginInfo(String input, String ip)
    {
        if(blacklist.contains(ip))
            return false;

        blacklist.add(ip);
        String[] data = input.split("\\;");
        if(data.length == 2 && dictonary.containsKey(data[0]))
        {
            return dictonary.get(data[0]).equals(data[1]);
        }

        return false;
    }

    @Override
    public void run()
    {
        blacklist.clear();
    }
}
