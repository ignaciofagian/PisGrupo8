package datatypes;

public class DTPaqueteRespuesta {
		private int	puntaje;
		private String eng;
		private String esp;
		
		public DTPaqueteRespuesta(String eng, String esp, int puntaje)
		{
			this.esp = esp;
			this.eng = eng;
			this.puntaje = puntaje;
		}
		public String getEng() {
			return eng;
		}
		public void setEng(String eng) {
			this.eng = eng;
		}
		public String getEsp() {
			return esp;
		}
		public void setEsp(String esp) {
			this.esp = esp;
		}
		public int getPuntaje() {
			return puntaje;
		}
		public void setPuntaje(int puntaje) {
			this.puntaje = puntaje;
		}
	
}
