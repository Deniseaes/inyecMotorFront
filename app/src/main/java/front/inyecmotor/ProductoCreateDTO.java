package front.inyecmotor;

import java.util.List;

public class ProductoCreateDTO {

        private int id;
        private String codigo;
        private String nombre;
        private double precioCosto; // Cambiado a double para manejar precisión en precios
        private double precioVenta; // Cambiado a double para manejar precisión en precios
        private int stockActual;
        private int stockMax;
        private int stockMin;
        // Otros campos...
        private List<Integer> idTipos;

        // Constructor vacío (puede ser útil para algunas operaciones)
        public ProductoCreateDTO() {
        }

        // Constructor con parámetros para inicializar todos los campos
        public ProductoCreateDTO(int id, String codigo, String nombre, double precioCosto, double precioVenta, int stockActual, int stockMax, int stockMin, List<Integer> idTipos) {
            this.id = id;
            this.codigo = codigo;
            this.nombre = nombre;
            this.precioCosto = precioCosto;
            this.precioVenta = precioVenta;
            this.stockActual = stockActual;
            this.stockMax = stockMax;
            this.stockMin = stockMin;
            this.idTipos = idTipos;
        }

        // Getters y setters para todos los campos

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCodigo() {
            return codigo;
        }

        public void setCodigo(String codigo) {
            this.codigo = codigo;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public double getPrecioCosto() {
            return precioCosto;
        }

        public void setPrecioCosto(double precioCosto) {
            this.precioCosto = precioCosto;
        }

        public double getPrecioVenta() {
            return precioVenta;
        }

        public void setPrecioVenta(double precioVenta) {
            this.precioVenta = precioVenta;
        }

        public int getStockActual() {
            return stockActual;
        }

        public void setStockActual(int stockActual) {
            this.stockActual = stockActual;
        }

        public int getStockMax() {
            return stockMax;
        }

        public void setStockMax(int stockMax) {
            this.stockMax = stockMax;
        }

        public int getStockMin() {
            return stockMin;
        }

        public void setStockMin(int stockMin) {
            this.stockMin = stockMin;
        }

        // Otros getters y setters si es necesario para otros campos


        public List<Integer> getIdTipos() {
            return idTipos;
        }

        public void setIdTipos(List<Integer> idTipos) {
            this.idTipos = idTipos;
        }
}
