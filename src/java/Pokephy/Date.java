package Pokephy;



public class Date {
  private int day;

  private int month;

  private int year;

  public Date(int day, int month, int year) {
    // Bouml preserved body begin 00020E03
	  this.day=day;
	  this.month=month;
	  this.year=year;
    // Bouml preserved body end 00020E03
  }
  
  public Date(String date) {
      String[] str = date.split("/");
      this.day = Integer.valueOf(str[0]);
      this.month = Integer.valueOf(str[1]);
      this.year = Integer.valueOf(str[2]);
  }

  /**
   * This function permits to check if the date in parameter is before or after this date.
   * Return TRUE if the date in parameter is not older, FALSE if not.
   */
  public boolean check_dates(Date date1) {
    // Bouml preserved body begin 0001F582
	  if (this.year < date1.year) return true;
	  
	  else if(this.year > date1.year) return false;
	  
	  else 
		  if(this.month < date1.month) return true;
		  else if (this.month > date1.month) return false;
		  else 
			  if(this.day < date1.day)return true;
			  else if (this.day > date1.day) return false;
			  else return true;
	  
	  
    // Bouml preserved body end 0001F582
  }

  /**
   * Permits to display the date.
   */
  public void printDate() {
    // Bouml preserved body begin 0002F902
	  System.out.print(this.day);
	  System.out.print("/");
	  System.out.print(this.month);
	  System.out.print("/");
	  System.out.println(this.year);

    // Bouml preserved body end 0002F902
  }

  /**
   * This function permit to check is the date in parameter is the same that this date.
   */
  public boolean compareDate(Date date) {
    // Bouml preserved body begin 00043902
	  if(this.day == date.day && this.month==date.month && this.year==date.year){
		  return true;
	  }
	  else return false;
	  
    // Bouml preserved body end 00043902
  }
  
  public int getDay(){
      return this.day;
  }
  public int getMonth(){
      return this.month;
  }
  public int getYear(){
      return this.year;
  }

  @Override
  public String toString(){
      return this.day+"/"+this.month+"/"+this.year;
  }
}