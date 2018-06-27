package tasks;

import java.util.Map;

/**
 * Created by Пендальф Синий on 27.06.2018.
 */
public abstract class AbstractTask implements Runnable{
    public Map<String, String> cookies;
    public int index;

    public AbstractTask(int index, Map<String, String> cookies) {

        this.index = index;
        this.cookies = cookies;
    }

    @Override
    public abstract void run();
}
