package vista;

import Controlador.ControladorEstadisticas;
import modelo.Login;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PanelEstadisticas extends JPanel {

    private static final Color GOLD = new Color(212, 175, 55);
    private static final Color DARK_ROYAL = new Color(12, 20, 51);
    private static final Color CONTENT_BG = new Color(240, 242, 247);
    private static final Color CARD_BORDER = new Color(229, 231, 235);
    private static final Color TEXT_MAIN = new Color(17, 24, 39);
    private static final Color TEXT_MUTED = new Color(107, 114, 128);
    private static final Color WHITE = Color.WHITE;
    private static final Color GREEN = new Color(34, 197, 94);
    private static final Color RED = new Color(239, 68, 68);
    private static final Color YELLOW = new Color(234, 179, 8);
    private static final Color BLUE = new Color(59, 130, 246);

    private final Login usuario;
    private final ControladorEstadisticas ctrl;
    private boolean dataLoaded = false;

    private int[] roomStats;
    private int activeRes, totalClients, totalEmployees, pendingInvoices, checkIns, checkOuts;
    private double totalRevenue, revenueDay, revenueWeek, revenueMonth;
    private LinkedHashMap<String, Double> monthlyRevenue;
    private LinkedHashMap<String, Integer> occupancyByType;
    private List<String[]> upcomingReservations, recentInvoices;

    public PanelEstadisticas(Login usuario) {
        this.usuario = usuario;
        this.ctrl = new ControladorEstadisticas();
        setLayout(new BorderLayout());
        setBackground(CONTENT_BG);

        JLabel loading = new JLabel("Cargando estad\u00edsticas...", SwingConstants.CENTER);
        loading.setFont(new Font("SansSerif", Font.ITALIC, 14));
        loading.setForeground(TEXT_MUTED);
        add(loading);

        SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                roomStats = ctrl.getRoomStats();
                activeRes = ctrl.getActiveReservations();
                totalClients = ctrl.getTotalClients();
                totalEmployees = ctrl.getTotalEmployees();
                totalRevenue = ctrl.getTotalRevenue();
                pendingInvoices = ctrl.getPendingInvoices();
                checkIns = ctrl.getCheckInsToday();
                checkOuts = ctrl.getCheckOutsToday();
                revenueDay = ctrl.getRevenueDay();
                revenueWeek = ctrl.getRevenueWeek();
                revenueMonth = ctrl.getRevenueMonth();
                monthlyRevenue = ctrl.getMonthlyRevenue12();
                occupancyByType = ctrl.getOccupancyByType();
                upcomingReservations = ctrl.getUpcomingReservations();
                recentInvoices = ctrl.getRecentInvoices();
                dataLoaded = true;
                return null;
            }
            @Override
            protected void done() {
                removeAll();
                setLayout(new BoxLayout(PanelEstadisticas.this, BoxLayout.Y_AXIS));
                setBorder(new EmptyBorder(20, 24, 24, 24));
                buildUI();
                revalidate();
                repaint();
            }
        };
        sw.execute();
    }

    private void buildUI() {
        if (!dataLoaded) return;
        boolean staff = !"cliente".equalsIgnoreCase(usuario.getRolUsuario());

        add(createWelcomeHeader());
        add(Box.createVerticalStrut(20));

        if (staff) {
            add(sectionTitle("Resumen del Hotel"));
            add(Box.createVerticalStrut(12));
            add(cardsRow(new CardDef[]{cd("\uD83C\uDFE0","Total Habitaciones",roomStats[0],DARK_ROYAL), cd("\u2705","Disponibles",roomStats[1],GREEN), cd("\uD83D\uDD11","Ocupadas",roomStats[2],RED), cd("\u26A0\uFE0F","Mantenimiento",roomStats[3],YELLOW)}));
            add(Box.createVerticalStrut(12));
            add(cardsRow(new CardDef[]{cd("\uD83D\uDC64","Clientes",totalClients,BLUE), cd("\uD83D\uDC68\u200D\uD83D\uDCBB","Empleados",totalEmployees,BLUE), cd("\uD83D\uDCB0","Ingresos Totales",totalRevenue,GREEN), cd("\uD83D\uDCC5","Reservas Activas",activeRes,GOLD)}));
            add(Box.createVerticalStrut(20));
            add(sectionTitle("Alertas del D\u00eda"));
            add(Box.createVerticalStrut(12));
            add(alertsRow());
            add(Box.createVerticalStrut(20));
            if ((monthlyRevenue != null && !monthlyRevenue.isEmpty()) || (occupancyByType != null && !occupancyByType.isEmpty())) {
                add(sectionTitle("Ingresos y Ocupaci\u00f3n"));
                add(Box.createVerticalStrut(12));
                add(chartsRow());
                add(Box.createVerticalStrut(20));
            }
            add(sectionTitle("Actividad Reciente"));
            add(Box.createVerticalStrut(12));
            add(tablesRow());
        } else {
            add(cardsRow(new CardDef[]{cd("\uD83C\uDFE0","Habitaciones",roomStats[0],DARK_ROYAL), cd("\u2705","Disponibles",roomStats[1],GREEN), cd("\uD83D\uDCC5","Mis Reservas",activeRes,GOLD)}));
        }
        add(Box.createVerticalGlue());
    }

    private JLabel sectionTitle(String t) { JLabel l=new JLabel(t); l.setFont(new Font("SansSerif",Font.BOLD,16)); l.setForeground(TEXT_MAIN); return l; }
    private static CardDef cd(String i,String t,long v,Color a){return new CardDef(i,t,String.valueOf(v),a);}
    private static CardDef cd(String i,String t,double v,Color a){return new CardDef(i,t,String.format("$%,.0f",v),a);}

    private JPanel createWelcomeHeader() {
        JPanel hero = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0,0,DARK_ROYAL,getWidth(),0,new Color(31,45,112)));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12); g2.dispose();
            }
        };
        hero.setLayout(new BorderLayout(12,8));
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE,110));

        JPanel left = new JPanel(new GridLayout(3,1,2,2)); left.setOpaque(false);
        JLabel hn = new JLabel("HOTEL GALES"); hn.setFont(new Font("SansSerif",Font.BOLD,22)); hn.setForeground(GOLD); left.add(hn);
        JLabel w = new JLabel("Bienvenido, "+usuario.getNombreUsuario()); w.setFont(new Font("SansSerif",Font.PLAIN,15)); w.setForeground(new Color(255,255,255,200)); left.add(w);
        JLabel rb = new JLabel("  "+usuario.getRolUsuario().toUpperCase()+"  "); rb.setFont(new Font("SansSerif",Font.BOLD,11)); rb.setForeground(DARK_ROYAL); rb.setOpaque(true); rb.setBackground(GOLD); rb.setBorder(BorderFactory.createEmptyBorder(3,10,3,10)); left.add(rb);
        hero.add(left,BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT,16,6)); right.setOpaque(false);
        boolean staff = !"cliente".equalsIgnoreCase(usuario.getRolUsuario());
        if (staff) { right.add(revBadge("Hoy",revenueDay)); right.add(revBadge("Semana",revenueWeek)); right.add(revBadge("Mes",revenueMonth)); }
        JButton r = new JButton("\u21BB"); r.setFont(new Font("SansSerif",Font.BOLD,16)); r.setForeground(GOLD); r.setBackground(new Color(255,255,255,30)); r.setBorder(BorderFactory.createLineBorder(new Color(GOLD.getRed(),GOLD.getGreen(),GOLD.getBlue(),100),1)); r.setFocusPainted(false); r.setCursor(new Cursor(Cursor.HAND_CURSOR)); r.setPreferredSize(new Dimension(40,40)); r.addActionListener(e -> refresh()); r.setToolTipText("Actualizar"); right.add(r);
        hero.add(right,BorderLayout.EAST);
        hero.setBorder(BorderFactory.createEmptyBorder(20,24,20,24));
        return hero;
    }

    private JPanel revBadge(String l, double v) {
        JPanel p = new JPanel(new BorderLayout(4,0)); p.setOpaque(false);
        JLabel lb = new JLabel(l); lb.setFont(new Font("SansSerif",Font.PLAIN,10)); lb.setForeground(new Color(255,255,255,180)); p.add(lb,BorderLayout.NORTH);
        JLabel va = new JLabel(String.format("$%,.0f",v)); va.setFont(new Font("SansSerif",Font.BOLD,16)); va.setForeground(GOLD); p.add(va,BorderLayout.CENTER);
        return p;
    }

    private static class CardDef { String icon,title,value; Color accent; CardDef(String i,String t,String v,Color a){icon=i;title=t;value=v;accent=a;} }

    private JPanel cardsRow(CardDef[] cards) {
        JPanel row = new JPanel(new GridLayout(1,cards.length,12,0)); row.setOpaque(false); row.setMaximumSize(new Dimension(Integer.MAX_VALUE,90));
        for (CardDef cd : cards) { JPanel c=new JPanel(new BorderLayout(8,4)); c.setBackground(WHITE); c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(CARD_BORDER,1),new EmptyBorder(14,16,14,16)));
            JPanel t=new JPanel(new BorderLayout(8,0)); t.setOpaque(false); JLabel ic=new JLabel(cd.icon); ic.setFont(new Font("SansSerif",Font.PLAIN,18)); t.add(ic,BorderLayout.WEST); JLabel tl=new JLabel(cd.title); tl.setFont(new Font("SansSerif",Font.PLAIN,12)); tl.setForeground(TEXT_MUTED); t.add(tl,BorderLayout.CENTER); c.add(t,BorderLayout.NORTH);
            JLabel vl=new JLabel(cd.value); vl.setFont(new Font("SansSerif",Font.BOLD,22)); vl.setForeground(cd.accent); c.add(vl,BorderLayout.CENTER); row.add(c); }
        return row;
    }

    private JPanel alertsRow() {
        JPanel row = new JPanel(new GridLayout(1,4,12,0)); row.setOpaque(false); row.setMaximumSize(new Dimension(Integer.MAX_VALUE,80));
        row.add(alert("\uD83D\uDC4B","Check-ins Hoy",checkIns,GREEN));
        row.add(alert("\uD83C\uDFC3","Check-outs Hoy",checkOuts,RED));
        row.add(alert("\u26A0\uFE0F","Mantenimiento",roomStats[3],YELLOW));
        row.add(alert("\uD83D\uDCB5","Pagos Pend.",pendingInvoices,RED));
        return row;
    }

    private JPanel alert(String icon, String title, long val, Color accent) {
        JPanel p=new JPanel(new BorderLayout(6,2)); p.setBackground(WHITE); p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(CARD_BORDER,1),new EmptyBorder(10,14,10,14)));
        JLabel ic=new JLabel(icon); ic.setFont(new Font("SansSerif",Font.PLAIN,14)); p.add(ic,BorderLayout.WEST);
        JPanel cen=new JPanel(new GridLayout(2,1)); cen.setOpaque(false);
        JLabel tl=new JLabel(title); tl.setFont(new Font("SansSerif",Font.PLAIN,11)); tl.setForeground(TEXT_MUTED); cen.add(tl);
        JLabel vl=new JLabel(String.valueOf(val)); vl.setFont(new Font("SansSerif",Font.BOLD,18)); vl.setForeground(accent); cen.add(vl);
        p.add(cen,BorderLayout.CENTER); return p;
    }

    private JPanel chartsRow() {
        JPanel row = new JPanel(new GridLayout(1,2,14,0)); row.setOpaque(false); row.setMaximumSize(new Dimension(Integer.MAX_VALUE,260));
        row.add(barChart("Ingresos Mensuales",monthlyRevenue,true));
        row.add(barChart("Reservas por Tipo",occupancyByType,false));
        return row;
    }

    private JPanel barChart(String title, LinkedHashMap<?,?> data, boolean currency) {
        JPanel p=new JPanel(new BorderLayout()); p.setBackground(WHITE); p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(CARD_BORDER,1),new EmptyBorder(14,16,14,16)));
        JLabel tl=new JLabel(title); tl.setFont(new Font("SansSerif",Font.BOLD,13)); tl.setForeground(TEXT_MAIN); p.add(tl,BorderLayout.NORTH);
        if (data==null||data.isEmpty()){JLabel e=new JLabel("Sin datos",SwingConstants.CENTER); e.setFont(new Font("SansSerif",Font.ITALIC,13)); e.setForeground(TEXT_MUTED); p.add(e,BorderLayout.CENTER); return p;}
        JPanel bars=new JPanel(); bars.setLayout(new BoxLayout(bars,BoxLayout.Y_AXIS)); bars.setBackground(WHITE); bars.setBorder(new EmptyBorder(6,0,0,0));
        double max=0; for(Object v:data.values()){double dv=v instanceof Number?((Number)v).doubleValue():0; if(dv>max)max=dv;} if(max<=0)max=1;
        int cnt=0;
        for(Map.Entry<?,?> e:((LinkedHashMap<?,?>)data).entrySet()){if(cnt>=8)break; String lb=e.getKey().toString(); double val=e.getValue() instanceof Number?((Number)e.getValue()).doubleValue():0; double rat=val/max;
            JPanel r=new JPanel(new BorderLayout(6,0)); r.setBackground(WHITE); r.setMaximumSize(new Dimension(Integer.MAX_VALUE,22));
            JLabel l=new JLabel(lb.length()>10?lb.substring(0,10)+"..":lb); l.setFont(new Font("SansSerif",Font.PLAIN,10)); l.setForeground(TEXT_MAIN); l.setPreferredSize(new Dimension(60,20)); r.add(l,BorderLayout.WEST);
            JPanel bo=new JPanel(new BorderLayout()); bo.setBackground(new Color(243,244,246)); bo.setPreferredSize(new Dimension(100,16));
            final double rr=rat; JPanel bf=new JPanel(){@Override protected void paintComponent(Graphics g){super.paintComponent(g); int w=(int)(getWidth()*rr); if(w>0){Graphics2D g2=(Graphics2D)g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON); g2.setColor(GOLD); g2.fillRoundRect(0,1,w,getHeight()-2,4,4); g2.dispose();}}};
            bf.setBackground(new Color(243,244,246)); bo.add(bf,BorderLayout.CENTER); r.add(bo,BorderLayout.CENTER);
            JLabel vl=new JLabel(currency?String.format("$%,.0f",val):String.valueOf((int)val)); vl.setFont(new Font("SansSerif",Font.BOLD,10)); vl.setForeground(TEXT_MAIN); vl.setHorizontalAlignment(SwingConstants.RIGHT); vl.setPreferredSize(new Dimension(60,20)); r.add(vl,BorderLayout.EAST);
            bars.add(r); bars.add(Box.createVerticalStrut(4)); cnt++;
        }
        p.add(bars,BorderLayout.CENTER); return p;
    }

    private JPanel tablesRow() {
        JPanel row = new JPanel(new GridLayout(1,2,14,0)); row.setOpaque(false); row.setMaximumSize(new Dimension(Integer.MAX_VALUE,280));
        row.add(tablePanel("Pr\u00f3ximas Reservas",new String[]{"ID","Cliente","Entrada","Salida","Hab.","Per."},upcomingReservations));
        row.add(tablePanel("\u00daltimas Facturas",new String[]{"ID","Fecha","Total","Estado","Pago"},recentInvoices));
        return row;
    }

    private JPanel tablePanel(String title, String[] cols, List<String[]> data) {
        JPanel p=new JPanel(new BorderLayout(0,8)); p.setBackground(WHITE); p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(CARD_BORDER,1),new EmptyBorder(14,16,14,16)));
        JLabel tl=new JLabel(title); tl.setFont(new Font("SansSerif",Font.BOLD,13)); tl.setForeground(TEXT_MAIN); p.add(tl,BorderLayout.NORTH);
        String[][] td; if(data==null||data.isEmpty()){td=new String[1][cols.length]; td[0][0]="Sin datos";}else{td=new String[data.size()][cols.length]; for(int i=0;i<data.size();i++){String[] r=data.get(i); System.arraycopy(r,0,td[i],0,Math.min(r.length,cols.length));}}
        JTable t=new JTable(td,cols); t.setFont(new Font("SansSerif",Font.PLAIN,11)); t.setForeground(TEXT_MAIN); t.setBackground(WHITE); t.setRowHeight(26); t.setShowGrid(true); t.setGridColor(new Color(243,244,246)); t.setSelectionBackground(new Color(253,230,138)); t.setSelectionForeground(TEXT_MAIN);
        t.getTableHeader().setFont(new Font("SansSerif",Font.BOLD,10)); t.getTableHeader().setForeground(WHITE); t.getTableHeader().setBackground(DARK_ROYAL); t.setEnabled(false);
        JScrollPane s=new JScrollPane(t); s.setBorder(null); p.add(s,BorderLayout.CENTER); return p;
    }

    private void refresh() {
        removeAll();
        setLayout(new BorderLayout());
        JLabel loading = new JLabel("Actualizando...", SwingConstants.CENTER);
        loading.setFont(new Font("SansSerif", Font.ITALIC, 14));
        loading.setForeground(TEXT_MUTED);
        add(loading);
        revalidate(); repaint();
        dataLoaded = false;
        SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() {
                roomStats = ctrl.getRoomStats(); activeRes = ctrl.getActiveReservations(); totalClients = ctrl.getTotalClients(); totalEmployees = ctrl.getTotalEmployees();
                totalRevenue = ctrl.getTotalRevenue(); pendingInvoices = ctrl.getPendingInvoices(); checkIns = ctrl.getCheckInsToday(); checkOuts = ctrl.getCheckOutsToday();
                revenueDay = ctrl.getRevenueDay(); revenueWeek = ctrl.getRevenueWeek(); revenueMonth = ctrl.getRevenueMonth();
                monthlyRevenue = ctrl.getMonthlyRevenue12(); occupancyByType = ctrl.getOccupancyByType(); upcomingReservations = ctrl.getUpcomingReservations(); recentInvoices = ctrl.getRecentInvoices();
                dataLoaded = true; return null;
            }
            @Override protected void done() {
                removeAll(); setLayout(new BoxLayout(PanelEstadisticas.this, BoxLayout.Y_AXIS)); setBorder(new EmptyBorder(20,24,24,24));
                buildUI(); revalidate(); repaint();
            }
        };
        sw.execute();
    }
}
