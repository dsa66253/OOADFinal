package BookingSystem;

import static java.lang.Integer.parseInt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Dictionary;

import org.json.JSONArray;
import org.json.JSONObject;

import backend.Ticket;
import backend.TicketManager;
import backend.Price;

public class BookTicket implements IBookTicketDictionary{
	
	private Price aPrice = new Price();
	private TicketManager tm = new TicketManager();
	
    private int CalPrice(String start, String end, String ticketType) {
        int outputPrice = 0;
        for (int i = 0; i < 12; i++){
            if (aPrice.get(i, "OriginStationID").equals(start)) {
                int j;
                for (j = 0; j < aPrice.getNumOfDestinationStations(i); j++) {
                    if (aPrice.get(i, j, "DesrinationStations", "ID").equals(end)) {
                        int k;
                        for (k = 0; k < 8; k++) {
                            if (aPrice.get(i, j, k, "DesrinationStations", "Fares", "TicketType").equals(ticketType)) {
                                outputPrice = parseInt(aPrice.get(i, j, k, "DesrinationStations", "Fares", "Price"));
                            }
                        }
                    }
                }
            }
        }
        
        return outputPrice;
    }
    
	/**  
	 * parameters: input: {"standard_ticket_count", "student_ticket_count", "uid", "train_number", 
	 * 						"departure_time", "arrival_time", "travel_time", 
	 * 						"start", "end", "seat"}
	*/
	//	return: boolean: successful booking or not
    @Override
	public Boolean selectCandidate(Dictionary<String, String> input) {
		// return boolean
		Boolean isDone = false;
		TicketManager tm = new TicketManager();
		
		// get today date/time
		Calendar cal = Calendar.getInstance();
		String timeStemp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
		String date = timeStemp.substring(0, 10);
		cal.add(Calendar.DATE, 3);
		String payDeadline = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
		
		// Set ticket standard
		int standard_count = Integer.parseInt((String) input.get("standard_ticket_count"));
		// Set ticket student 
		int student_count = Integer.parseInt((String) input.get("student_ticket_count"));
					
		if(standard_count != 0) {
			isDone = true;
			// Price standard Calculator
			int standard_pirce = CalPrice(input.get("start"), input.get("end"), "standard");
			
			for(int i=0; i<standard_count; i++) {
				System.out.println("standard_count "+ standard_count);
				Ticket t = new Ticket();
				t.setCode(input.get("uid"));
				t.setUid(input.get("uid"));
				t.setTicketInfo(0, "date", date);
				t.setTicketInfo(0, "ticketsType", "standard");
				t.setTicketInfo(0, "start", input.get("start"));
				t.setTicketInfo(0, "end", input.get("end"));
				t.setTicketInfo(0, "seats", input.get("seat"));
				t.setTicketInfo(0, "departureTime", input.get("departure_time"));
				t.setTicketInfo(0, "arrivalTime", input.get("arrival_time"));
				t.setPayDeadLine(payDeadline);
				t.setPayment(standard_pirce);
				tm.addTicket(t);
				
			}
			
		}
		
		
		if(student_count != 0) {
			// Price standard Calculator
			isDone = true;
			int student_pirce = CalPrice(input.get("start"), input.get("end"), "0.8");
			
			for(int i=0; i<student_count; i++) {
				System.out.println("student_count "+ student_count);
				Ticket t = new Ticket();
				t.setCode(input.get("uid"));
				t.setUid(input.get("uid"));
				t.setTicketInfo(0, "data", date);
				t.setTicketInfo(0, "ticketsType", "student");
				t.setTicketInfo(0, "start", input.get("start"));
				t.setTicketInfo(0, "end", input.get("end"));
				t.setTicketInfo(0, "seats", input.get("seat"));
				t.setTicketInfo(0, "departureTime", input.get("departure_time"));
				t.setTicketInfo(0, "arrivalTime", input.get("arrival_time"));
				t.setPayDeadLine(payDeadline);
				t.setPayment(student_pirce);
				tm.addTicket(t);
				
			}
			tm.save("Booking_test.json");
		}
		
		return isDone;
		
	}

	private int BookingHistoryID(String code, String uid) throws wrongInputException {
		//    	System.out.println("tm.toString() " + tm.toString());
				for (int i = 0; i < tm.getSize(); i++) {
					if (tm.getTicketObj(i).getCode().equals(code) && tm.getTicketObj(i).getUid().equals(uid)) {
						return i;
					}
				}
				throw new wrongInputException("NO BOOKING HISTORY FOUND");
			}
		
			public String BookingHistory(String code, String uid) {
				String result = "";
				try {
					int idx = BookingHistoryID(code, uid);
					result = "\nBooking Code: " + tm.getTicketObj(idx).getCode();
					result = result + "\nUser ID: " + tm.getTicketObj(idx).getUid();
					result = result + "\nDate: " + tm.getTicketObj(idx).getTicketInfo(0, "date");
					result = result + "\nTickets Type: " + tm.getTicketObj(idx).getTicketInfo(0, "ticketsType");
					result = result + "\nSeats: " + tm.getTicketObj(idx).getTicketInfo(0, "seats");
					result = result + "\nPay Deadline: " + tm.getTicketObj(idx).getPayDeadLine();
					result = result + "\nPayment: " + tm.getTicketObj(idx).getPayment() + "\n";
				} catch (wrongInputException e) {
					result = "Not found!\n";
				}
		
				return result;
			}

	public String Unbooking(String code, String uid) {
		int idx;
		String result = "";
		System.out.println(code + " "+ uid);
		try {
			result = BookingHistory( code,  uid);
			idx = BookingHistoryID(code, uid);
			System.out.println(tm.getSize());
			tm.removeTicket(idx);
			System.out.println(tm.getSize());
			tm.save();
		} catch (wrongInputException e) {
			System.out.println("Not found!\n"+ e);
			result = "Not found!\n";
		}
		return result;
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
	}






}
