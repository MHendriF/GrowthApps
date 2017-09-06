package Master;

/**
 *   PT Trikarya Teknologi on 4/21/2016.
 */
public class Logging {
    private int kd_logging;
    private String description,log_time;

    public Logging(int kd_logging, String description, String log_time) {
        this.kd_logging = kd_logging;
        this.description = description;
        this.log_time = log_time;
    }

    public int getKd_logging() {
        return kd_logging;
    }

    public void setKd_logging(int kd_logging) {
        this.kd_logging = kd_logging;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLog_time() {
        return log_time;
    }

    public void setLog_time(String log_time) {
        this.log_time = log_time;
    }
}
