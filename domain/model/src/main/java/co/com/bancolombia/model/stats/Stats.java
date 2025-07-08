package co.com.bancolombia.model.stats;

import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder
@With
public class Stats {
    private int totalContactoClientes;
    private int motivoReclamo;
    private int motivoGarantia;
    private int motivoDuda;
    private int motivoCompra;
    private int motivoFelicitaciones;
    private int motivoCambio;
    private String hash;
    // Campo adicional para la clave primaria de DynamoDB (timestamp)
    private long timestamp;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Stats{");
        sb.append("totalContactoClientes=").append(totalContactoClientes);
        sb.append(", motivoReclamo=").append(motivoReclamo);
        sb.append(", motivoGarantia=").append(motivoGarantia);
        sb.append(", motivoDuda=").append(motivoDuda);
        sb.append(", motivoCompra=").append(motivoCompra);
        sb.append(", motivoFelicitaciones=").append(motivoFelicitaciones);
        sb.append(", motivoCambio=").append(motivoCambio);
        sb.append('}');
        return sb.toString();
    }
}
