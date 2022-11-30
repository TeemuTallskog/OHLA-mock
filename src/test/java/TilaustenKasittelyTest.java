import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TilaustenKasittelyTest {
    @Mock
    IHinnoittelija hinnoittelijaMock;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testaaKasittelijaOver100(){
        float alkuSaldo = 200.0f;
        float listaHinta = 100.0f;
        float alennus = 20.0f;
        float loppuSaldo = alkuSaldo - (listaHinta * (1 - (alennus + 5) / 100));
        Asiakas asiakas = new Asiakas(alkuSaldo);
        Tuote tuote = new Tuote("test", listaHinta);

        doAnswer(invocationOnMock -> {
            float prosentti = (float)invocationOnMock.getArguments()[1];
            when(hinnoittelijaMock.getAlennusProsentti(asiakas, tuote))
                    .thenReturn(prosentti);
            return null;
        }).when(hinnoittelijaMock).setAlennusProsentti(any(), anyFloat());
        when(hinnoittelijaMock.getAlennusProsentti(asiakas,tuote))
                .thenReturn(alennus);

        TilaustenKäsittely käsittely = new TilaustenKäsittely();
        käsittely.setHinnoittelija(hinnoittelijaMock);
        käsittely.käsittele(new Tilaus(asiakas, tuote));

        assertEquals(loppuSaldo, asiakas.getSaldo(), 0.001);
        verify(hinnoittelijaMock, atLeastOnce()).getAlennusProsentti(asiakas, tuote);
        verify(hinnoittelijaMock, atLeastOnce()).setAlennusProsentti(asiakas, alennus + 5);
    }
    @Test
    public void testaaKasittelijaUnder100(){
        float alkuSaldo = 200.0f;
        float listaHinta = 99.0f;
        float alennus = 20.0f;
        float loppuSaldo = alkuSaldo - (listaHinta * (1 - alennus / 100));
        Asiakas asiakas = new Asiakas(alkuSaldo);
        Tuote tuote = new Tuote("test", listaHinta);


        doAnswer(invocationOnMock -> {
            float prosentti = (float)invocationOnMock.getArguments()[1];
            when(hinnoittelijaMock.getAlennusProsentti(asiakas, tuote))
                    .thenReturn(prosentti);
            return null;
        }).when(hinnoittelijaMock).setAlennusProsentti(any(), anyFloat());

        when(hinnoittelijaMock.getAlennusProsentti(asiakas,tuote))
                .thenReturn(alennus);

        TilaustenKäsittely käsittely = new TilaustenKäsittely();
        käsittely.setHinnoittelija(hinnoittelijaMock);
        käsittely.käsittele(new Tilaus(asiakas, tuote));

        assertEquals(loppuSaldo, asiakas.getSaldo(), 0.001);
        verify(hinnoittelijaMock, atLeastOnce()).getAlennusProsentti(asiakas, tuote);
    }
}
