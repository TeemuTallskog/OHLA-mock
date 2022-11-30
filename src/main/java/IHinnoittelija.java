public interface IHinnoittelija {
	public abstract float getAlennusProsentti(Asiakas asiakas, Tuote tuote);

	void setAlennusProsentti(Asiakas asiakas, float v);

	void aloita();

	void lopeta();
}
