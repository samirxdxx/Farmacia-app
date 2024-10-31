package Imprimir;

import Imprimirl.Params;
import java.util.Map;
import javax.swing.JDialog;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class ReportManager {

    private static ReportManager instance;

    private JasperReport report;

    public static ReportManager getInstance() {
        if (instance == null) {
            instance = new ReportManager();
        }
        return instance;
    }

    private ReportManager() {
    }

    public void compileReport(String fileJasperReport) throws JRException {
        report = JasperCompileManager.compileReport(getClass().getResourceAsStream(fileJasperReport));

    }

    public void printReport(Params data, Map para) throws JRException {
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data.getFields());
            JasperPrint print = JasperFillManager.fillReport(report, para, dataSource);
            view(print);

        } catch (JRException e) {
            e.printStackTrace();
            throw new JRException(e.getMessage());
        }

    }

    private void view(JasperPrint print) throws JRException {
        JasperViewer jasperViewer = new JasperViewer(print, false);
        JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setContentPane(jasperViewer.getContentPane());
        dialog.setSize(jasperViewer.getSize());
        dialog.setTitle("Vista Preliminar");
        dialog.setVisible(true);

    }
}
