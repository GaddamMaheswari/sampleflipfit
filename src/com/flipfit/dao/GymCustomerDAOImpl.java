package com.flipfit.dao;
<<<<<<< HEAD

import com.flipfit.bean.GymSlots;
import com.flipfit.business.GymSlotsBusiness;
import com.flipfit.business.GymSlotsBusinessImpl;
import com.flipfit.exceptions.*;
=======
import com.flipfit.exception.DBconnectionException;
import com.flipfit.exception.InvalidInputException;
import com.flipfit.exception.ResourceNotFoundException;
import com.flipfit.exception.UnauthorisedAccessException;
>>>>>>> 01950559380923ee0553e2dda53c2355c1df21fd
import com.flipfit.utils.DBConnection;
import com.flipfit.bean.GymBooking;
import com.flipfit.bean.GymCustomer;
import com.flipfit.bean.GymPayment;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GymCustomerDAOImpl implements GymCustomerDAO {
    private Connection conn = null;
    private PreparedStatement statement = null;

<<<<<<< HEAD
    @Override
    public boolean createProfile(GymCustomer customer) throws InvalidCredentialsException, DataEntryFailedException {
=======
        // Checks if a booking with the given bookingID exists in the database
    public boolean IfBoookingExists(int bookingID) {
        try {
            conn = DBConnection.connect();
            statement = conn.prepareStatement("select * from bookings where BookingId = ?");
            statement.setInt(1, bookingID);  // Set the bookingID parameter
            // Execute the query
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return false;  // No booking found
            }
            return true;   // Booking found
        } catch (SQLException| DBconnectionException e) {
            throw new RuntimeException(e);
        }

    }
        // Checks if a customer with the given customerID exists in the database
    public boolean IfCustomerExists(int customerID) {
        try {
            conn = DBConnection.connect();
            statement = conn.prepareStatement("select * from Customer where CustId = ?");
            statement.setInt(1, customerID);  // Set the customerID parameter
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();   // Returns true if customer exists
        } catch (SQLException| DBconnectionException e) {
            throw new RuntimeException(e);
        }

    }

        //    Name , Email , Address , Phone Number , Password
        // Method to create a new customer profile
    @Override
    public void createProfile(GymCustomer customer) {
>>>>>>> 01950559380923ee0553e2dda53c2355c1df21fd
        try {
            // check whether user with the mail id exists

            conn = DBConnection.connect();
            statement = conn.prepareStatement("Select * From Registration where EmailAddress = ?");
            statement.setString(1, customer.getCustomerEmailAddress());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                throw new InvalidCredentialsException("User already exists with this email address");
            }

            //adds the user data into user table
            System.out.println("Adding User Profile");
<<<<<<< HEAD
            statement = conn.prepareStatement("insert into User(`Name`,`Email`,`PhoneNumber`,`Role`,`Address`) values (?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, customer.getCustomerName());
            statement.setString(2, customer.getCustomerEmailAddress());
            statement.setString(3, customer.getCustomerPhone());
            statement.setString(4, "gymcustomer");
            statement.setString(5, customer.getCustomerAddress());
            int rowsAffected = statement.executeUpdate();
            int customerId = 0;
            if (rowsAffected > 0) {
                // Retrieve the generated customerId
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    customerId = generatedKeys.getInt(1);
                }
            } else {
                throw new DataEntryFailedException("Failed Adding User Details to User Database");
            }

            // adds the data into customer table
            statement = conn.prepareStatement("insert into Customer values (?,?,?,?,?,?)");
            statement.setInt(1, customerId);
=======
            String emailAddress = customer.getCustomerEmailAddress();

            if(emailAddress == null || !emailAddress.contains("@") || !emailAddress.contains("."))
                throw new InvalidInputException("Email address cannot be null or empty");



            statement = conn.prepareStatement("insert into Customer values (?,?,?,?,?,?)");

            statement.setInt(1, 100);
>>>>>>> 01950559380923ee0553e2dda53c2355c1df21fd
            statement.setString(2, customer.getCustomerName());
            statement.setString(3, customer.getCustomerEmailAddress());
            statement.setString(4, customer.getCustomerAddress());
            statement.setString(5, customer.getCustomerPhone());
            statement.setString(6, customer.getPassword());
            statement.executeUpdate();

            // adds the emailId, userId and password to registration table
            statement = conn.prepareStatement("insert into Registration values (?,?,?,?)");
            statement.setInt(1, customerId);
            statement.setString(2, customer.getCustomerEmailAddress());
            statement.setString(3, customer.getPassword());
            statement.setString(4, "gymcustomer");
            statement.executeUpdate();
            return true;
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (DBConnectionException e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public boolean editProfile(GymCustomer customer) throws DataEntryFailedException {
        String sql = "UPDATE Customer SET Name = ?, Email = ?, Address = ?, PhoneNumber = ?, Password=? WHERE CustId = ?";

        try {

            conn = DBConnection.connect();
            // update the user details in customer table
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, customer.getCustomerName());
            statement.setString(2, customer.getCustomerEmailAddress());
            statement.setString(3, customer.getCustomerAddress());
            statement.setString(4, customer.getCustomerPhone());
            statement.setString(5, customer.getPassword());
            statement.setInt(6, customer.getCustomerId());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated <= 0) {
                throw new DataEntryFailedException("Failed to update profile in Customer Database");
            }

<<<<<<< HEAD
            // update the user details in user table
            statement = conn.prepareStatement("UPDATE User SET Name = ?, Email = ?, Address = ?, PhoneNumber = ? WHERE UserId = ?");
            statement.setString(1, customer.getCustomerName());
            statement.setString(2, customer.getCustomerEmailAddress());
            statement.setString(3, customer.getCustomerAddress());
            statement.setString(4, customer.getCustomerPhone());
            statement.setInt(5, customer.getCustomerId());
            statement.executeUpdate();

            // update the user details in registration table
            statement = conn.prepareStatement("UPDATE Registration SET EmailAddress = ?, Password = ? WHERE UserId = ?");
            statement.setString(1, customer.getCustomerEmailAddress());
            statement.setString(2, customer.getPassword());
            statement.setInt(3, customer.getCustomerId());
            statement.executeUpdate();
=======
        } catch (SQLException | InvalidInputException | DBconnectionException se ) {
            se.printStackTrace();
        }


    }
    @Override
    public void viewBookings(int customerId) {
        try {

            conn = DBConnection.connect();
            System.out.println("Adding User Profile");
            statement = conn.prepareStatement("select * from bookings where CustomerId = ?");
            statement.setInt(1, customerId);
            statement.executeQuery();
//            resultSet = statement.executeQuery();

//            while (resultSet.next()) {
//               Depedning on the columns of booking extract and print below
//                int bookingId = resultSet.getInt("BookingId");
//                String bookingDate = resultSet.getString("BookingDate");
            // You can print out other fields as necessary
//                System.out.println("Booking ID: " + bookingId + ", Booking Date: " + bookingDate);
//            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean waitlistStatus(int bookingID) {
        try {
//          Waitlist table :  BookingID , WaitListed Status
            if(!IfBoookingExists(bookingID))
                throw new ResourceNotFoundException("Booking ID already exists");

            conn = DBConnection.connect();
            System.out.println("Checking Waitlisted");
            statement = conn.prepareStatement("select * from waitlist where BookingID = ?");
            statement.setInt(1, bookingID);
//            statement.executeQuery();
            ResultSet resultSet = statement.executeQuery();

            return resultSet.getInt("Status") > 0;

        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }


    @Override
    public boolean cancelBooking(int bookingID , int customerID ) {
        try {
//          unauthAccess : to make sure user is cancelling their own bookings only . (take userid as parms also)
            if(!IfCustomerExists(customerID))
                throw new UnauthorisedAccessException("UnAutherised Access!");
            conn = DBConnection.connect();
            System.out.println("Cancel Booking...");
            statement = conn.prepareStatement("DELETE FROM Bookings where BookingID=?");
            statement.setInt(1, bookingID);
            statement.executeQuery();
>>>>>>> 01950559380923ee0553e2dda53c2355c1df21fd
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DBConnectionException e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public List<GymBooking> viewBookings(int customerId) throws ResourceNotFoundException {
        List<GymBooking> bookings = new ArrayList<>();
        try {
            // Fetch all bookings with the matching customerId
            conn = DBConnection.connect();
            statement = conn.prepareStatement("select * from CustomerBooking where CustId = ?");
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                // For each booking, get the center details
                statement = conn.prepareStatement("select * from GymCenters where centerId = ?");
                statement.setInt(1, resultSet.getInt(3));
                ResultSet resultSet1 = statement.executeQuery();
                resultSet1.next();
                String CenterName = resultSet1.getString(3);
                String CenterLoc = resultSet1.getString(4);

                // For each booking, get the slot details
                statement = conn.prepareStatement("select * from Slots where slotsId = ?");
                statement.setInt(1, resultSet.getInt(4));
                resultSet1 = statement.executeQuery();
                resultSet1.next();
                LocalTime startTime = resultSet1.getTime(3).toLocalTime();
                LocalTime endTime = resultSet1.getTime(4).toLocalTime();
                bookings.add(new GymBooking(resultSet.getInt(2), CenterName, CenterLoc, startTime, endTime, resultSet.getDate(5)));
            }
            if(bookings.isEmpty()){
                throw new ResourceNotFoundException("No bookings found");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (DBConnectionException e) {
            System.out.println(e);
        }
        return bookings;
    }

    @Override
    public boolean cancelBooking(int customerId, int bookingID) throws InvalidCredentialsException, UnauthorisedAccessException {
        try {

            conn = DBConnection.connect();

            // Fetch bookings with the matching customerId and bookingId
            statement = conn.prepareStatement("select * from CustomerBooking where BookingID = ?");
            statement.setInt(1, bookingID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int custId = resultSet.getInt(1);
                if(custId != customerId){
                    throw new UnauthorisedAccessException("You are not authorized to cancel bookings of other customers");
                }
                // Get the slot of booking cancelled
                statement = conn.prepareStatement("SELECT * from CustomerBooking where BookingId = ?");
                statement.setInt(1, bookingID);
                ResultSet resultSet1 = statement.executeQuery();
                if (!resultSet1.next()) {
                    return false;
                }
                int slotId = resultSet1.getInt(4);
                Date date = resultSet1.getDate(5);
                // cancel the booking
                statement = conn.prepareStatement("DELETE FROM CustomerBooking where BookingID=?");
                statement.setInt(1, bookingID);
                statement.executeUpdate();
                // finding available seats
                statement = conn.prepareStatement("Select * from AvailableSeats where slotId=? and Date=?");
                statement.setInt(1, slotId);
                statement.setDate(2,new java.sql.Date(date.getTime()));
                resultSet1 = statement.executeQuery();
                resultSet1.next();
                int availableSeats = resultSet1.getInt(3)+1;
                //update available seats
                statement = conn.prepareStatement("update AvailableSeats set NumSeats=? where slotId=? and Date=?");
                statement.setInt(1, availableSeats);
                statement.setInt(2, slotId);
                statement.setDate(3,new java.sql.Date(date.getTime()));
                statement.executeUpdate();
                return true;
            } else {
                throw new InvalidCredentialsException("Check your booking Id again");
            }
        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        } catch (DBConnectionException e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public int createBooking(int customerID, int slotID, int centerId, Date date) throws ResourceNotFoundException {
        try {

//        // 1. CenterID and SlotID Check (not implemented in this method, assumed as valid)
//        // 2. Check if a booking already exists for the given slot and date

            conn = DBConnection.connect();
            // If a booking was done for the slot and date
            statement = conn.prepareStatement("select * from AvailableSeats where slotId=? and Date=?");
            statement.setInt(1, slotID);
            statement.setDate(2, new java.sql.Date(date.getTime()));
            ResultSet resultSet = statement.executeQuery();
            int bookingID = 0;
            //If booking was done and available seats > 0
            if (resultSet.next() && resultSet.getInt(3) > 0) {
                int numSeats = resultSet.getInt(3);
                // decrement available seats by 1
                statement = conn.prepareStatement("update AvailableSeats set NumSeats = ? where slotId=? and Date=?");
                statement.setInt(1, numSeats - 1);
                statement.setInt(2, slotID);
                statement.setDate(3, new java.sql.Date(date.getTime()));
                statement.executeUpdate();
                //create a new booking
                System.out.println("Creating New Booking ");
                statement = conn.prepareStatement("insert into CustomerBooking(`CustId`,`centerId`,`slotId`,`Date`) values (?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
                statement.setInt(1, customerID);
                statement.setInt(2, centerId);
                statement.setInt(3, slotID);
                statement.setDate(4, new java.sql.Date(date.getTime()));
                int rowsAffected = statement.executeUpdate();
                //return booking Id
                if (rowsAffected > 0) {
                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        bookingID = generatedKeys.getInt(1);
                    }
                }
            } else if (!resultSet.next()) { // If no booking was done for the slot and date
                // Find total seats avaialble in slot
                statement = conn.prepareStatement("select * from slots where slotsId = ?");
                statement.setInt(1, slotID);
                resultSet = statement.executeQuery();
                if (!resultSet.next()) {
                    System.out.println("Something went wrong");
                }
                int TotalSeats = resultSet.getInt(5);
                // Insert new slot into available seats table with updated available seats
                statement = conn.prepareStatement("insert into AvailableSeats(`slotId`,`Date`,`NumSeats`) values (?,?,?)");
                statement.setInt(1, slotID);
                statement.setDate(2, new java.sql.Date(date.getTime()));
                statement.setInt(3, TotalSeats - 1);
                statement.executeUpdate();
                // create a new booking
                statement = conn.prepareStatement("insert into CustomerBooking(`CustId`,`centerId`,`slotId`,`Date`) values (?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
                statement.setInt(1, customerID);
                statement.setInt(2, centerId);
                statement.setInt(3, slotID);
                statement.setDate(4, new java.sql.Date(date.getTime()));
                //return the booking Id
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        bookingID = generatedKeys.getInt(1);
                    }
                }
            } else {
                throw new ResourceNotFoundException("No Seats Available for the slot on that date");
            }
            return bookingID;

        } catch (SQLException se) {
            se.printStackTrace();
            return -1;
        } catch (DBConnectionException e) {
            System.out.println(e);
            return -1;
        }
    }

    @Override
<<<<<<< HEAD
    public int makepayment(GymPayment paymentData) throws DataEntryFailedException {
        try {
            // check whether booking exists with bookingId

            conn = DBConnection.connect();

            System.out.println("Making Payment ");
            statement = conn.prepareStatement("Select * from CustomerBooking where BookingId = ?");
            statement.setInt(1, paymentData.getBookingID());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            // get the cost from slot table using slotId
            int slotId = resultSet.getInt(4);
            statement = conn.prepareStatement("SELECT * from Slots where slotsId = ?");
            statement.setInt(1, slotId);
            resultSet = statement.executeQuery();
            resultSet.next();
            int cost = resultSet.getInt(6);
            // add payment into table
            statement = conn.prepareStatement("insert into payment(`BookingId`, `Mode`, `Amount`) values (?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, paymentData.getBookingID());
            statement.setString(2, paymentData.getMode());
            statement.setInt(3, cost);
            int rowsAffected = statement.executeUpdate();
            int paymentId = 0;
            // return the paymentId
            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    paymentId = generatedKeys.getInt(1);
                }
            } else {
                throw new DataEntryFailedException("Data Entry Failed into the Payments Database");
            }
            return paymentId;
//            payment ID needs to auto generated and Mode has to be added in the table and status we need to add here
        } catch (SQLException se) {
            se.printStackTrace();
=======
    public int modifyBooking(int bookingID, int customerID, int centerID, int slotID) {
//  unauthAccess, ResourceNotFound .
        try {
            cancelBooking(bookingID , customerID );
            int newBookingID = createBooking(customerID, centerID, slotID);
            return newBookingID;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
>>>>>>> 01950559380923ee0553e2dda53c2355c1df21fd
        }
        catch (DBConnectionException e) {
            System.out.println(e);
        }
        return -1;
    }

    @Override
<<<<<<< HEAD
    public boolean updatepwd(String email, String password, String role) throws InvalidCredentialsException {
=======
    public boolean makepayment(GymPayment paymentData , int CustomerID) {
>>>>>>> 01950559380923ee0553e2dda53c2355c1df21fd
        try {

            if(!IfCustomerExists(CustomerID))
                throw new UnauthorisedAccessException("UnAutherised Access!");

//          unauthAccess, resourceNotFound(bookingID),
            conn = DBConnection.connect();
            statement = conn.prepareStatement("Select * from Registration where EmailAddress=? and role=?");
            statement.setString(1, email);
            statement.setString(2, role);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new InvalidCredentialsException("You are not registered for this role yet!!");
            } else {
                int id = resultSet.getInt(1);
                statement = conn.prepareStatement("update Registration set Password=? where UserId=?");
                statement.setString(1, password);
                statement.setInt(2, id);
                statement.executeUpdate();

                statement = conn.prepareStatement("update Customer set Password=? where CustId=?");
                statement.setString(1, password);
                statement.setInt(2, id);
                statement.executeUpdate();
                return true;
            }

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (DBConnectionException e) {
            System.out.println(e);
        }
        return false;
    }
}