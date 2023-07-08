package User2;
public class Registro{
	private String nombrecompleto;
		private String correoelectronico;
		private String password;

		public Registro(){


		}
           public Registro( String nombrecompleto,String correoelectronico,String password){
           	this.nombrecompleto= nombrecompleto;
           	this.correoelectronico=correoelectronico;
           	this.password=password;
           }public void setnombrecompleto(String nombrecompleto){
            	this.nombrecompleto = nombrecompleto;
            }public String getnombrecompleto(){
            	return  nombrecompleto;
            	
            
            }public void setcorreoelectronico(String correoelectronico){
            	this.correoelectronico=correoelectronico;
            }public String getcorreoelectronico(){
            	return correoelectronico;

            }public void setpassword(String password){
            	this.password = password;
            }public String getpassword(){
            	return  password;
            }
           }