# Flujo del Módulo de Compras y Ventas con Documentos

```mermaid
flowchart LR
  %% Nodos con esquinas redondeadas y colores suaves
  classDef nodo fill:#F5F9FF,stroke:#7BA7FF,color:#1F2D3D,stroke-width:1px;

  prov(Proveedor<br/>- Factura<br/>- Guía de envío)
  comp(Compras<br/>- Orden de compra<br/>- Recepción)
  inv(Inventario<br/>- Entrada/Salida de stock<br/>- Nota de almacén)
  cont(Contabilidad<br/>- Asientos contables<br/>- Cuentas por pagar/cobrar)
  vent(Ventas<br/>- Pedido de cliente<br/>- Factura de venta)
  cli(Cliente<br/>- Recibo<br/>- Nota de entrega)

  prov --> comp --> inv --> cont --> vent --> cli

  class prov,comp,inv,cont,vent,cli nodo;
```

Instrucciones
- Abre este archivo en un visor Markdown con soporte Mermaid, o usa https://mermaid.live para exportar a PNG/SVG.
- También disponible como página HTML en la ruta `/docs/flujo` dentro de la aplicación.
