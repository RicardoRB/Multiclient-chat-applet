/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MultiCliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JApplet;

/**
 *
 * @author Ricardo
 */
public class MultiCliente extends JApplet {

    private final int PUERTOUDP = 55557;

    /**
     * Initialization method that will be called after the applet is loaded into
     * the browser.
     */
    public void init() {
        // TODO start asynchronous download of heavy resources
        initComponents();
        Hilo hilo = new Hilo();
        hilo.start();
        this.getRootPane().setDefaultButton(btn_enviar);
    }
    // TODO overwrite start(), stop() and destroy() methods

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        ep_chat = new javax.swing.JEditorPane();
        btn_enviar = new javax.swing.JButton();
        tf_enviar = new javax.swing.JTextField();

        setMinimumSize(new java.awt.Dimension(600, 400));

        ep_chat.setContentType("text/html"); // NOI18N
        jScrollPane1.setViewportView(ep_chat);

        btn_enviar.setText("Enviar");
        btn_enviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_enviarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1)
                .addGroup(layout.createSequentialGroup()
                .addComponent(tf_enviar, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_enviar)))
                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(tf_enviar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btn_enviar))));

    }// </editor-fold>    

    private void btn_enviarActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        enviar(tf_enviar.getText().getBytes());
        tf_enviar.setText("");
    }

    public void enviar(byte[] dato) {
        try {
            ep_chat.setText(ep_chat.getText().replaceAll("<body>", "").replaceAll("</body>", "").replaceAll("<html>", "").replaceAll("</html>", "").replaceAll("<head>", "").replaceAll("</head>", "") + "<b>Enviando dato...</b> <br>");
            MulticastSocket enviador = new MulticastSocket();

            // Usamos la direccion Multicast 230.0.0.1, por poner alguna dentro del rango
            // y el puerto 55557, uno cualquiera que esté libre.
            DatagramPacket dgp = new DatagramPacket(dato, dato.length, InetAddress.getByName("230.0.0.1"), PUERTOUDP);
            // Envío
            enviador.send(dgp);
        } catch (IOException ex) {
            Logger.getLogger(MultiCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public class Hilo extends Thread {

        public void run() {
            while (true) {
                recibir();
            }
        }

        public void recibir() {
            try {
                // Un array de bytes con tamaño suficiente para recoger el mensaje enviado, 
                // bastaría con 4 bytes.
                byte[] dato = new byte[1024];

                // El mismo puerto que se uso en la parte de enviar.
                MulticastSocket escucha = new MulticastSocket(PUERTOUDP);

                // Nos ponemos a la escucha de la misma IP de Multicast que se uso en la parte de enviar.
                escucha.joinGroup(InetAddress.getByName("230.0.0.1"));

                // Se espera la recepción. La llamada a receive() se queda
                // bloqueada hasta que llegue un mesnaje.
                DatagramPacket dgp = new DatagramPacket(dato, dato.length);
                ep_chat.setText(ep_chat.getText().replaceAll("<body>", "").replaceAll("</body>", "").replaceAll("<html>", "").replaceAll("</html>", "").replaceAll("<head>", "").replaceAll("</head>", "") + "<b>Esperando dato...</b> <br>");
                escucha.receive(dgp);

                // Obtención del dato ya relleno.
                String rec = new String(dgp.getData(), "ISO-8859-1").trim();
                ep_chat.setText(ep_chat.getText().replaceAll("<body>", "").replaceAll("</body>", "").replaceAll("<html>", "").replaceAll("</html>", "").replaceAll("<head>", "").replaceAll("</head>", "") + "<b>Recibiendo dato...</b> <br>");
                ep_chat.setText(ep_chat.getText().replaceAll("<body>", "").replaceAll("</body>", "").replaceAll("<html>", "").replaceAll("</html>", "").replaceAll("<head>", "").replaceAll("</head>", "") + rec + "<br>");
                //enviar(dgp.getData());
            } catch (IOException ex) {
                Logger.getLogger(MultiCliente.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    // Variables declaration - do not modify                     
    private javax.swing.JButton btn_enviar;
    private javax.swing.JEditorPane ep_chat;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField tf_enviar;
    // End of variables declaration       
}
