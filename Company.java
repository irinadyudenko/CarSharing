package carsharing;

public class Company {
    private String name;
    private int id;

    Company (int id, String name) {
        this.name = name;
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getName() {
        return  this.name;
    }


    public int getId() {
        return this.id;
    }
}
