package se.abalon.cache.dao;


public class WorkPlaceDAO
{

    public Long workplace_id;
    public String workplace_number;
    public String terminal_terminalnumber;

    public Long getWorkplace_id() {
        return workplace_id;
    }

    public void setWorkplace_id(Long workplace_id) {
        this.workplace_id = workplace_id;
    }

    public String getWorkplace_number() {
        return workplace_number;
    }

    public void setWorkplace_number(String workplace_number) {
        this.workplace_number = workplace_number;
    }

    public String getTerminal_terminalnumber() {
        return terminal_terminalnumber;
    }

    public void setTerminal_terminalnumber(String terminal_terminalnumber) {
        this.terminal_terminalnumber = terminal_terminalnumber;
    }
}
