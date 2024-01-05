package com.employee.service;

import com.employee.entity.EmployeePayrollData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class EmployeePayrollService {

    public enum IOService {CONSOLE_IO, FILE_IO, DB_IO}
    private List<EmployeePayrollData> employeePayrollList;

    private final EmployeePayrollDBService employeePayrollDBService;
    public EmployeePayrollService(){
        employeePayrollDBService = EmployeePayrollDBService.getInstance();
    }
    public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
        this();
        this.employeePayrollList = employeePayrollList;
    }

    public static void main(String[] args) {
        ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
        Scanner consoleInputReader = new Scanner(System.in);
        employeePayrollService.readEmployeePayrollData(consoleInputReader);
        employeePayrollService.writeEmployeePayrollData(IOService.FILE_IO);
    }

    public void readEmployeePayrollData(Scanner consoleInputReader){
        System.out.println("Enter Employee ID: ");
        int id = consoleInputReader.nextInt();
        System.out.println("Enter Employee name: ");
        String name = consoleInputReader.next();
        System.out.println("Enter Employee salary");
        double salary = consoleInputReader.nextDouble();
        employeePayrollList.add(new EmployeePayrollData(id, name, salary)); //asking for the emp details and creating object out of it and adding to list
    }

    public void writeEmployeePayrollData(IOService ioService){
        if(ioService.equals(IOService.CONSOLE_IO)){
            System.out.println("\nWriting Employee Payroll roaster to Console\n"+ employeePayrollList);

        }else if(ioService.equals(IOService.FILE_IO)){
            new EmployeePayrollFileService().writeData(employeePayrollList);
        }
    }

    public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService){
        if(ioService.equals(IOService.DB_IO)){
            this.employeePayrollList = employeePayrollDBService.readData();
        }
        return this.employeePayrollList;
    }
    public void printData(IOService ioService){
        if(ioService.equals(IOService.FILE_IO)){
            new EmployeePayrollFileService().printData();
        }
    }

    public long countEntries(IOService ioService){
        if(ioService.equals(IOService.FILE_IO)){
            return new EmployeePayrollFileService().countEntries();
        }
        return 0;
    }

    public boolean checkEmployeePayrollInSyncWithDB(String name) {
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
        return employeePayrollList.get(0).equals(getEmployeePayrollData(name));
    }

    // UC3: Update salary
    public void updateEmployeeSalary(String name, double salary) {
        int result = employeePayrollDBService.updateEmployeeData(name, salary);
        if(result == 0) return;

        EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
        if(employeePayrollData != null){
            employeePayrollData.salary = salary;
        }
    }

    // List to Stream
    private EmployeePayrollData getEmployeePayrollData(String name) {
        return this.employeePayrollList.stream()
                .filter(employeePayrollDataItem -> employeePayrollDataItem.name.equals(name))
                .findFirst()
                .orElse(null);
    }

    // UC5: Select from Date Range
    public List<EmployeePayrollData> readEmployeePayrollForDateRange(IOService ioService,
                                                                     LocalDate startDate, LocalDate endDate) {
        if(ioService.equals(IOService.DB_IO)){
            return employeePayrollDBService.getEmployeePayrollForDateRange(startDate, endDate);
        }
        return null;
    }

//    UC6: Get average salary by gender
    public Map<String, Double> readAverageSalaryByGender(IOService ioService) {
        if(ioService.equals(IOService.DB_IO)){
            return employeePayrollDBService.getEmployeeAverageSalaryByGender();
        }
        return null;
    }
}