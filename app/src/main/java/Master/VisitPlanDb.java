package Master;


import java.util.Calendar;

/**
 *   PT Trikarya Teknologi on 3/23/2016.
 */
public class VisitPlanDb {
    private int kd_visit, approve_visit, status_visit, kd_outlet;
    private String skip_reason, skip_order_reason,date_visit, date_visiting, date_created;

    public VisitPlanDb(int kd_visit,int kd_outlet,String date_visit) {
        setKd_visit(kd_visit);
        setKd_outlet(kd_outlet);
        setDate_visit(date_visit);
        setApprove_visit(2);
        setStatus_visit(0);
        Calendar calendar = Calendar.getInstance();
        setDate_created(String.valueOf(calendar.get(calendar.YEAR))+"-"+String.valueOf(calendar.get(calendar.MONTH)+1)+"-"+String.valueOf(calendar.get(calendar.DAY_OF_MONTH)+1)+
        " "+String.valueOf(calendar.get(calendar.HOUR_OF_DAY))+":"+String.valueOf(calendar.get(calendar.MINUTE))+":"+String.valueOf(calendar.get(calendar.SECOND)));
    }

    public VisitPlanDb(int kd_visit,int kd_outlet,String date_visit,String date_created, int approve_visit, int status_visit, String date_visiting, String skip_order_reason, String skip_reason) {
        setKd_visit(kd_visit);
        setKd_outlet(kd_outlet);
        setDate_visit(date_visit);
        setDate_created(date_created);
        setApprove_visit(approve_visit);
        setStatus_visit(status_visit);
        setDate_visiting(date_visiting);
        setSkip_order_reason(skip_order_reason);
        setSkip_reason(skip_reason);
    }

    public int getKd_visit() {
        return kd_visit;
    }

    public void setKd_visit(int kd_visit) {
        this.kd_visit = kd_visit;
    }

    public int getApprove_visit() {
        return approve_visit;
    }

    public void setApprove_visit(int approve_visit) {
        this.approve_visit = approve_visit;
    }

    public int getStatus_visit() {
        return status_visit;
    }

    public void setStatus_visit(int status_visit) {
        this.status_visit = status_visit;
    }

    public int getKd_outlet() {
        return kd_outlet;
    }

    public void setKd_outlet(int kd_outlet) {
        this.kd_outlet = kd_outlet;
    }

    public String getDate_visit() {
        return date_visit;
    }

    public void setDate_visit(String date_visit) {
        this.date_visit = date_visit;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getDate_visiting() {
        return date_visiting;
    }

    public void setDate_visiting(String date_visiting) {
        this.date_visiting = date_visiting;
    }

    public String getSkip_order_reason() {
        return skip_order_reason;
    }

    public void setSkip_order_reason(String skip_order_reason) {
        this.skip_order_reason = skip_order_reason;
    }

    public String getSkip_reason() {
        return skip_reason;
    }

    public void setSkip_reason(String skip_reason) {
        this.skip_reason = skip_reason;
    }
}
