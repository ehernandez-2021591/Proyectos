package User2;
public class Perfil extends Registro{

	private int numerotelefonico;
	private String rol ;
	private int edad;

	public Perfil(){

	}
	public Perfil(String nombrecompleto,  String correoelectronico, String password, int numerotelefonico, String rol, int edad){
       super (nombrecompleto,correoelectronico,password);
       this.numerotelefonico = numerotelefonico;
       this.rol=rol;
       this.edad=edad;
	}
       public void setnumerotelefonico(int numerotelefonico){
       	this.numerotelefonico=numerotelefonico;

       }
       public int getnumerotelefonico(){
       	return numerotelefonico;
       }
       public void setrol(String rol){
       	this.rol=rol;
       }
        public String getrol(){
        	return rol;
        }
         public void setedad(int edad){
         	 this.edad=edad;

         }
              public int getedad(){
              	return edad;
              }

                

}