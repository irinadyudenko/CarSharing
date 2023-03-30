package carsharing;


import java.sql.SQLException;
import java.util.ArrayList;

class CarSharingUI {

    CompanyDao companyDao = new CompanyDaoImpl();
    CarDao carDao = new CarDaoImpl();
    CustomerDao customerDao = new CustomerDaoImpl();

    Company currentCompany = null;

    Customer currentCustomer = null;

    String mainMenuStatement = "1. Company list\n" +
            "2. Create a company\n" +
            "0. Back";
    String loginStatement = "1. Log in as a manager\n" +
            "2. Log in as a customer\n" +
            "3. Create a customer\n" +
            "0. Exit";
    String createCompanyStatement = "Enter the company name:";
    String createCarStatement = "Enter the car name:";
    String createCustomerStatement = "Enter the customer name:";
    String customerMenuStatement = "1. Rent a car\n" +
            "2. Return a rented car\n" +
            "3. My rented car\n" +
            "0. Back";


    private enum Menu_States {LOGIN, MAIN_MENU,
        CREATE_COMPANY, COMPANY_LIST_MENU,
        CAR_MENU, CREATE_CAR,
        CUSTOMER_LIST, CREATE_CUSTOMER, CUSTOMER_MENU,
        CHOOSE_COMPANY_MENU, CHOOSE_CAR_MENU}
    Menu_States currentState = Menu_States.LOGIN;
    public boolean proceedUserChoice(String choice, DB database) throws SQLException {
        switch (currentState) {
            case LOGIN  : {
                currentCustomer = null;
                switch (choice) {
                    case "0" : {
                        return false;
                    }
                    case "1" : {
                        currentState = Menu_States.MAIN_MENU;
                        System.out.println(mainMenuStatement);
                        return true;
                    }
                    case "2" : {
                        if ( printCustomerList(customerDao.getAllCustomers(database)) ) {
                            System.out.println("0. Back");
                            currentState = Menu_States.CUSTOMER_LIST;
                        }
                        else {
                            System.out.println(loginStatement);
                            currentState = Menu_States.LOGIN;
                        }
                        return true;
                    }
                    case "3" : {
                        System.out.println(createCustomerStatement);
                        currentState = Menu_States.CREATE_CUSTOMER;
                        return true;
                    }
                }
            }
            case MAIN_MENU : {
                currentCompany = null;
                switch (choice) {
                    case "0" : {
                        currentState = Menu_States.LOGIN;
                        System.out.println(loginStatement);
                        return true;
                    }
                    case "1" : {
                        if (companyDao.printAllCompanies(database)) {
                            currentState = Menu_States.COMPANY_LIST_MENU;
                        }
                        else {
                            System.out.println(mainMenuStatement);
                            currentState = Menu_States.MAIN_MENU;
                        }
                        return true;
                    }
                    case "2" : {
                        currentState = Menu_States.CREATE_COMPANY;
                        System.out.println(createCompanyStatement);
                        return true;
                    }
                }
            }
            case CREATE_COMPANY : {
                companyDao.addCompany(choice, database);
                currentState = Menu_States.MAIN_MENU;
                System.out.println(mainMenuStatement);
                return true;
            }
            case COMPANY_LIST_MENU : {
                if (choice.equals("0")) {
                    currentState = Menu_States.MAIN_MENU;
                    System.out.println(mainMenuStatement);
                    return true;
                }
                if (currentCompany == null) {
                    currentCompany = companyDao.getCompany(Integer.parseInt(choice), database);
                }
                String companyName = currentCompany.getName();
                printCarMenu(companyName);
                currentState = Menu_States.CAR_MENU;
                return true;
            }
            case CAR_MENU : {
                switch (choice) {
                    case "0" : {
                        currentState = Menu_States.MAIN_MENU;
                        System.out.println(mainMenuStatement);
                        return true;
                    }
                    case "1" : {
                        carDao.printAllCars(currentCompany.getId(), database, "Car list:", false);
                        printCarMenu(currentCompany.getName());
                        currentState = Menu_States.CAR_MENU;
                        return true;
                    }
                    case "2" : {
                        currentState = Menu_States.CREATE_CAR;
                        System.out.println(createCarStatement);
                        return true;
                    }
                }
            }
            case CREATE_CAR : {
                carDao.addCar(choice, currentCompany.getId(), database);
                currentState = Menu_States.CAR_MENU;
                printCarMenu(currentCompany.getName());
                return true;
            }
            case CUSTOMER_LIST : {
                if (choice.equals("0")) {
                    System.out.println(loginStatement);
                    currentState = Menu_States.LOGIN;
                }
                else {
                    System.out.println(customerMenuStatement);
                    currentState = Menu_States.CUSTOMER_MENU;
                    currentCustomer = customerDao.getCustomer(Integer.parseInt(choice), database);
                }
                return true;
            }
            case CREATE_CUSTOMER : {
                customerDao.addCustomer(choice, database);
                currentState = Menu_States.LOGIN;
                System.out.println(loginStatement);
                return true;

            }
            case CUSTOMER_MENU : {
                switch (choice) {
                    case "0" : {
                        System.out.println(loginStatement);
                        currentState = Menu_States.LOGIN;
                        return true;
                    }
                    case "1" : {
                        if (!(currentCustomer.getRentedCarId()>0)) {
                            if (companyDao.printAllCompanies(database)) {
                                currentState = Menu_States.CHOOSE_COMPANY_MENU;
                            } else {
                                System.out.println(customerMenuStatement);
                                currentState = Menu_States.CUSTOMER_MENU;
                            }
                        }
                        else {
                            System.out.println("You've already rented a car!");
                            System.out.println(customerMenuStatement);
                            currentState = Menu_States.CUSTOMER_MENU;
                        }
                        return true;

                    }
                    case "2" : {
                        if (customerDao.returnRentedCar(currentCustomer.getId(), currentCustomer.getRentedCarId(), database)) {
                            System.out.println("You've returned a rented car!");
                            currentCustomer.setIsCarReturned(true);
                        }
                        else {
                            if (currentCustomer.getIsCarReturned()) {
                                System.out.println("You've returned a rented car!");
                            }
                            System.out.println("You didn't rent a car!");
                        }
                        System.out.println(customerMenuStatement);

                        currentState = Menu_States.CUSTOMER_MENU;
                        return true;
                    }
                    case "3" : {
                        if (!customerDao.getRentedCar(currentCustomer.getId(), database)) {
                            System.out.println("You didn't rent a car!");
                            System.out.println(customerMenuStatement);
                        }
                        System.out.println(customerMenuStatement);
                        currentState = Menu_States.CUSTOMER_MENU;
                        return true;
                    }
                }
                return true;
            }
            case CHOOSE_COMPANY_MENU: {
                if (choice.equals("0")) {
                    System.out.println(customerMenuStatement);
                    currentState = Menu_States.CUSTOMER_MENU;
                }
                else {
                    int companyID = Integer.parseInt(choice);
                    carDao.printAllCars(companyID, database, "Choose a car:", true);
                    currentState = Menu_States.CHOOSE_CAR_MENU;
                }
                return true;
            }
            case CHOOSE_CAR_MENU: {
                if (!choice.equals("0")) {
                    int rentedCarID = Integer.parseInt(choice);
                    currentCustomer.setRentedCarId(rentedCarID);
                    customerDao.setRentedCar(currentCustomer.getId(), rentedCarID, database);
                    System.out.println("You rented '" + carDao.getCarName(rentedCarID, database) + "'");
                    currentCustomer.setIsCarReturned(false);
                    currentState = Menu_States.CUSTOMER_MENU;
                }
                currentState = Menu_States.CUSTOMER_MENU;
                System.out.println(customerMenuStatement);
                return true;
            }
        }
        return true;
    }

    void printCarMenu(String companyName) {
        String companyListMenuStatement = "'" + companyName + "' company\n" +
                "1. Car list\n" +
                "2. Create a car\n" +
                "0. Back";
        System.out.println(companyListMenuStatement);
    }

    boolean printCustomerList(ArrayList<Customer> customerArrayList) {
        if (customerArrayList.isEmpty()) {
            System.out.println("The customer list is empty!");
            return false;
        }
        else {
            System.out.println("Choose a customer:");
            for(Customer customer : customerArrayList) {
                System.out.println(customer.getId() + ". " + customer.getName());
            }
            return true;
        }
    }


}

