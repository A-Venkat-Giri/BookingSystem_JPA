package com.dev.bss.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.dev.bbs.exceptions.CustomException;
import com.dev.bss.beans.Available;
import com.dev.bss.beans.Booking;
import com.dev.bss.beans.Bus;
import com.dev.bss.beans.Feedback;
import com.dev.bss.beans.Ticket;
import com.dev.bss.beans.User;
import com.dev.bss.sevice.AdminServicImpl;
import com.dev.bss.sevice.ServiceAdmin;
import com.dev.bss.sevice.ServiceUser;
import com.dev.bss.sevice.UserServiceImpl;

public class App {


	public static User addUser() {
		Scanner sc=new Scanner(System.in);
		User user = new User();
		ServiceUser services = new UserServiceImpl();
		System.out.println("Enter Details to Register");
		System.out.println("UserName");
		user.setUserName(sc.next());
		System.out.println("Email");
		user.setEmail(services.checkEmail(sc.next()));
		System.out.println("password");
		user.setPassword(sc.next());
		System.out.println("Enter Contact");
		user.setContact(Long.parseLong(services.checkContact(sc.next())));

		return user;
	}




	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		int userId;
		String password;
		ServiceUser service=new UserServiceImpl();
		ServiceAdmin adminService=new AdminServicImpl();
		Boolean loop=true;

		while(loop)
		{
			System.out.println("********************************************");
			System.out.println("** BUS BOOKING SYSTEM                     **");
			System.out.println("********************************************");
			System.out.println("** [1] Login                              **");
			System.out.println("** [2] Create Account                     **");
			System.out.println("** [9] Admin Login                        **");
			System.out.println("** [10] Exit                              **");
			System.out.println("********************************************");
			System.out.println("********************************************");
			System.out.print("Enter Choice :");
			int firstChoice=sc.nextInt();
			if(firstChoice==1)
			{
				System.out.println("Enter UserId");
				userId=Integer.parseInt(service.checkUserIdAndBookinIdAndBusId(sc.next()));
				System.out.println("Enter password");
				password=sc.next();
				Boolean login=service.loginUser(userId,password);
				System.out.println("Login"+login);
				while(login)
				{	
					System.out.println("********************************************");
					System.out.println("** BUS BOOKING SYSTEM                     **");
					System.out.println("********************************************");
					System.out.println("** [1] Update Info                        **");
					System.out.println("** [2] Delete Profile                     **");
					System.out.println("** [3] User Details                       **");
					System.out.println("** [4] Book Ticket                        **");
					System.out.println("** [5] View Ticket                        **");
					System.out.println("** [6] Cancel Ticket                      **");
					System.out.println("** [7] Give Feedback                      **");
					System.out.println("** [8] Logout                             **");
					System.out.println("********************************************");
					System.out.println("********************************************");
					System.out.print("Enter Choice :");
					int choice =sc.nextInt();
					if(choice==1)
					{

						System.out.println("For Authentication add Password");
						password=sc.next();
						User user = new User();
						user.setUserId(userId);
						System.out.println("Enter New UserName");
						user.setUserName(sc.next());
						System.out.println("Enter New Email");
						user.setEmail(service.checkEmail(sc.next()));
						System.out.println("Enter new Contact");
						user.setContact(Long.parseLong(service.checkContact(sc.next())));
						System.out.println("Enter new Password");
						String newPassword = sc.next();

						if(service.updateUser(user, password , newPassword ))
						{
							System.out.println("Info Updated");
						}
						else {
							System.out.println("Failed Please Try Again....");
						}
					}
					else if(choice==2)
					{

						System.out.println("To Confirm your request Type Password");
						password=sc.next();
						if(service.deleteUser(userId,password))
						{
							System.out.println("Account Deleted");
							login =  false;
						}
						else
						{
							System.out.println("Failed Please Try Again....");
						}
					}
					else if(choice==3)
					{
						User user=service.searchUser(userId);
						if(user != null)
						{
							System.out.println("UserId : "+user.getUserId());
							System.out.println("Username : "+user.getUserName());
							System.out.println("Email : "+user.getEmail());
							System.out.println("Contact : "+user.getContact());
						}

					}
					else if(choice==4)
					{

						try {
							Ticket ticket = new Ticket();
							ticket.setUserId(userId);
							System.out.println("Enter Source");
							String source = sc.next();
							ticket.setSource(source);
							System.out.println("Enter Destination");
							String destination =sc.next();
							ticket.setDestination(destination);
							System.out.println("Enter Date");
							String date2 = sc.next();
							Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date2);
							ticket.setDate(date1);
							java.sql.Date date = new java.sql.Date(date1.getTime());
							List<Bus> buses = service.searchBus(source, destination, date);
							System.out.println("Available Bus");
							for(Bus bus:buses)
							{
								System.out.println(bus);
							}
							System.out.println("Enter Bus id");
							Integer busId = Integer.parseInt(service.checkUserIdAndBookinIdAndBusId(sc.next()));
							ticket.setBusId(busId);
							Integer availSeats = service.checkAvailability(busId, date);
							System.out.println("Available Seats : "+availSeats);
							System.out.println("Enter number of tickets");
							Integer numOfTickets = Integer.parseInt(service.checkUserIdAndBookinIdAndBusId(sc.next()));
							ticket.setNumberOfSeats(numOfTickets);
							ticket.setAvailable(availSeats);
							if(numOfTickets <= availSeats)
							{
								Booking state = service.bookTicket(ticket);
								Bus bus = new Bus();
								if(state != null)
								{

									System.out.println("Booking Successfull");
									System.out.println(state);
								}
								else
								{
									System.out.println("Failed Please Try Again....");
								}
							}
						} catch (ParseException e) {

							throw new CustomException("WrongDateFormat");
						}

					}
					else if(choice==5)
					{
						System.out.println("Enter Booking Id");
						Booking booking=service.getTicket(Integer.parseInt(service.checkUserIdAndBookinIdAndBusId(sc.next())));
						if(booking != null)
						{
							System.out.println(booking);
						}
						else
						{
							System.out.println("No Such Booking Found");
						}


					}
					else if(choice==6)
					{
						List<Booking> bookings = service.getAllTickets(userId);
						for(Booking booking:bookings)
						{
							System.out.println(booking);
						}
						System.out.println("Enter Booking Id To Delete");
						Boolean b=service.cancelTicket(Integer.parseInt(service.checkUserIdAndBookinIdAndBusId(sc.next())));
						if(b)
						{
							System.out.println("Ticket Cancelled");
						}
						else
						{
							System.out.println("Failed Please Try Again....");
						}


					}
					if(choice==7)
					{

						System.out.println("Write Feedback");
						Scanner scan = new Scanner(System.in);
						String feedback = scan.nextLine();
						System.out.println("\n");
						Feedback feedb = new Feedback();
						feedb.setUserId(userId);
						feedb.setFeedback(feedback);
						Boolean update = service.giveFeedBack(feedb);
						if(update)
						{
							System.out.println("Feedbaack Added Succesfully");
						}
						else
						{
							System.out.println("Failed to give FeedBack");
						}
					}
					else if(choice==8)
					{
						login=false;
					}
				

				}
			}
			else if(firstChoice==2)
			{
				User user = addUser();
				user = service.createUser(user);
				if(user!= null)
				{
					System.out.println("User Added");
					System.out.println(user);
				}
				else
				{
					System.out.println("Failed Please Try Again....");
				}
			}
			else if(firstChoice == 9)
			{   

				System.out.println("Enter Admin Id");
				int adminId=Integer.parseInt(adminService.checkUserIdAndBookinIdAndBusId(sc.next()));
				System.out.println("Enter Admin Password");
				String AdminPassword=sc.next();
				Boolean	adminLogin = adminService.adminLogin(adminId, AdminPassword);
				System.out.println("Admin Login :"+adminLogin);
				while(adminLogin) {
					System.out.println("********************************************");
					System.out.println("** BUS BOOKING SYSTEM                     **");
					System.out.println("********************************************");
					System.out.println("** [1] Add Bus                            **");
					System.out.println("** [2] Search Bus                         **");
					System.out.println("** [3] Update Bus Info                    **");
					System.out.println("** [4] Delete Bus                         **");
					System.out.println("** [5] Search Bus between                 **");
					System.out.println("** [6] Get Feedbacks                      **");
					System.out.println("** [7] Logout                             **");
					System.out.println("********************************************");
					System.out.println("********************************************");
					System.out.print("Enter Choice :");
					int adminChoice = sc.nextInt();
					if(adminChoice == 1)
					{
						Bus bus = new Bus();
						System.out.println("bus Name");
						bus.setBusName(sc.next());
						System.out.println("Bus Type");
						bus.setBusType(sc.next());
						System.out.println("Source");
						bus.setSource(sc.next());
						System.out.println("Destination");
						bus.setDestination(sc.next());
						System.out.println("Total Seats");
						bus.setTotalSeats(Integer.parseInt(adminService.checkUserIdAndBookinIdAndBusId(sc.next())));
						System.out.println("Price");
						bus.setPrice(Integer.parseInt(adminService.checkUserIdAndBookinIdAndBusId(sc.next())));
						Bus create = adminService.createBus(bus);
						System.out.println(create);
						if(create != null)
						{

							try {
								System.out.println("Bus Added Successfully");
								System.out.println("----------------------------------------------");
								Available available = new Available();
								System.out.println("Enter Total Seats");
								available.setAvailSeats(Integer.parseInt(adminService.checkUserIdAndBookinIdAndBusId(sc.next())));
								available.setBusId(create.getBusId());
								System.out.println("Enter Date");
								Date date;
								date = new SimpleDateFormat("yyyy-MM-dd").parse(sc.next());
								available.setJourneyDate(date);
								Boolean state = adminService.addAvailability(available);
								if(state)
								{
									System.out.println("Available Added Successfully");
								}
								else
								{
									System.out.println("Failed TO add");
								}

							} catch (ParseException e) {
								e.printStackTrace();
							}
						}else
						{
							System.out.println("Failed Please Try Again....");
						}
					}

					else if(adminChoice == 2)
					{
						System.out.println("Enter bus Id");
						Bus bus=adminService.searchBus(Integer.parseInt(adminService.checkUserIdAndBookinIdAndBusId(sc.next())));
						if(bus != null)
						{
							System.out.println("Bus [busId=" + bus.getBusId() + ", Bus Name=" + bus.getBusName() + 
									", source=" + bus.getSource() + ", destination=" + bus.getDestination()
									+ ", bus Type=" + bus.getBusType() + ", Seats=" + bus.getTotalSeats()+ ", price=" + bus.getPrice() +  "]");

						}

					}
					else if(adminChoice == 3)
					{
						System.out.println("Enter bus Id");
						Bus bus=adminService.searchBus(Integer.parseInt(adminService.checkUserIdAndBookinIdAndBusId(sc.next())));
						System.out.println(" New bus Name");
						bus.setBusName(sc.next());
						System.out.println(" New Bus Type");
						bus.setBusType(sc.next());
						System.out.println("New Source");
						bus.setSource(sc.next());
						System.out.println(" New Destination");
						bus.setDestination(sc.next());
						System.out.println("New Price");
						bus.setPrice(Integer.parseInt(adminService.checkUserIdAndBookinIdAndBusId(sc.next())));

						if(bus != null)
						{
							Boolean updateBus=adminService.updateBus(bus);
							if(updateBus)
							{
								System.out.println("Bus Info Updated");
							}
							else
							{
								System.out.println("Failed Please Try Again....");
							}

						}

					}
					else if(adminChoice == 4)
					{
						System.out.println("Enter bus Id");
						int busId =Integer.parseInt(adminService.checkUserIdAndBookinIdAndBusId(sc.next()));
						System.out.println("Enter password To delete Bus");
						String userPassword = sc.next();
						if(AdminPassword.equals(userPassword))
						{
							Boolean del = adminService.deletebus(busId, userPassword);
							if(del)
							{
								System.out.println("Bus Deleted");
							}
							else
							{
								System.out.println("Failed Please Try Again....");
							}
						}

					}
					else if(adminChoice == 5)
					{
						System.out.println("Enter Source ");
						String source = sc.next();
						System.out.println("Enter Destination");
						String destination = sc.next();
						List<Bus> bus = adminService.busBetween(source, destination);
						System.out.println(bus);

					}
					else if(adminChoice == 6)
					{
						List<Feedback> feedbacks = adminService.showFeedback();
						for(Feedback fb : feedbacks)
						{
							System.out.println(fb);
						}
					}
					else if(adminChoice == 7)
					{
						adminLogin=false;
					}


				}

			}
			else if(firstChoice == 10)
			{
				loop = false;
			
			}
			else {
				System.out.println("Command Entered is Invalid");
			}
		}
	}

}

