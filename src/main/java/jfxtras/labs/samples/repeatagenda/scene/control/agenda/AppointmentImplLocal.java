package jfxtras.labs.samples.repeatagenda.scene.control.agenda;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Appointment;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.AppointmentGroup;

/**
 *  LocalTime start and end time for an appointment
 * @author David Bal
 *
 */
public class AppointmentImplLocal extends AppointmentImplBase<AppointmentImplLocal>
implements Appointment
{
    private static Map<Integer, Integer> appointmentGroupCount = new HashMap<Integer, Integer>();
    
    /** StartDateTime: */
    public ObjectProperty<LocalDateTime> startLocalDateTimeProperty() { return startLocalDateTime; }
    final private ObjectProperty<LocalDateTime> startLocalDateTime = new SimpleObjectProperty<LocalDateTime>(this, "startDateTime");
    public LocalDateTime getStartLocalDateTime() { return startLocalDateTime.getValue(); }
    public void setStartLocalDateTime(LocalDateTime value) { startLocalDateTime.setValue(value); }
    public AppointmentImplLocal withStartLocalDateTime(LocalDateTime value) { setStartLocalDateTime(value); return this; }
    
    /** EndDateTime: */
    public ObjectProperty<LocalDateTime> endLocalDateTimeProperty() { return endLocalDateTime; }
    final private ObjectProperty<LocalDateTime> endLocalDateTime = new SimpleObjectProperty<LocalDateTime>(this, "endDateTime");
    public LocalDateTime getEndLocalDateTime() { return endLocalDateTime.getValue(); }
    public void setEndLocalDateTime(LocalDateTime value) { endLocalDateTime.setValue(value); }
    public AppointmentImplLocal withEndLocalDateTime(LocalDateTime value) { setEndLocalDateTime(value); return this; } 
    
    /** Location: */
    // I'M NOT USING THESE
    public ObjectProperty<String> locationProperty() { return locationObjectProperty; }
    final private ObjectProperty<String> locationObjectProperty = new SimpleObjectProperty<String>(this, "location");
    public String getLocation() { return locationObjectProperty.getValue(); }
    public void setLocation(String value) { locationObjectProperty.setValue(value); }
    public AppointmentImplLocal withLocation(String value) { setLocation(value); return this; } 
    public AppointmentImplLocal() { }

    /**
     * Copy constructor for a repeatable appointment
     * 
     * @param repeat
     * @param date 
     */
    public AppointmentImplLocal(Repeat repeat, LocalDate date)
    {
        repeat.getAppointmentData().copyInto(this);
        LocalDateTime myStartDateTime = date.atTime(repeat.getStartLocalTime());
        LocalDateTime myEndDateTime = date.atTime(repeat.getEndLocalTime());
        
        this.withStartLocalDateTime(myStartDateTime)
            .withEndLocalDateTime(myEndDateTime)
            .withRepeat(repeat)
            .withRepeatMade(true);
    }
    
    /**
     * Copy constructor
     * 
     * @param appointment
     */
    public AppointmentImplLocal(Appointment appointment) {
        appointment.copyInto(this);
    }
    
    public String toString()
    {
        return super.toString()
             + ", "
             + this.getStartLocalDateTime()
             + " - "
             + this.getEndLocalDateTime()
             ;
    }

    protected static void writeToFile(Collection<Appointment> appointments, Path file)
    {
        // XML document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
//            Main.log.log(Level.SEVERE, "Can't build appointmentMap factory" , e);
        }
        Document doc = builder.newDocument();

        Element rootElement = doc.createElement("appointments");
        doc.appendChild(rootElement);
        
        // Appointments
        for (Appointment myAppointment : appointments)
        {
            if (myAppointment.isRepeatMade()) continue; // skip appointments that are made by repeat rules
            Element appointmentElement = doc.createElement("appointment");
            AppointmentFactory.returnConcreteAppointment(myAppointment).marshal(appointmentElement);
            rootElement.appendChild(appointmentElement);
        }

        String repeatKeys = appointments
                .stream()
                .filter(a -> ! a.isRepeatMade())
                .map(a -> a.getKey().toString()).collect(Collectors.joining(" "));
        rootElement.setAttribute("keys", repeatKeys);
        
        try {
            writeDocument(doc, file);
        } catch (TransformerException e) {
//            Main.log.log(Level.SEVERE, "Can't write appointmentMap file=" + file, e);
        }
    }
    
    /**
     * Writes a org.w3c.dom.Document to a output file.
     * 
     * @param doc
     * @param file
     * @throws TransformerException
     */
    public static void writeDocument(Document doc, Path file) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        if (Settings.PRETTY_XML) {
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        }
        
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file.toFile());
        transformer.transform(source, result);
    }
    
    protected Element marshal(Element myElement)
    {
        super.marshal(myElement);
        myElement.setAttribute(endLocalDateTime.getName(), DataUtilities.myFormatLocalDateTime(getEndLocalDateTime()));
        myElement.setAttribute(startLocalDateTime.getName(), DataUtilities.myFormatLocalDateTime(getStartLocalDateTime()));
        return myElement;
    }
    
    /**
     * Read in an appointment map from a file.  Adds elements to appointments
     * 
     * @param file
     * @param appointmentGroups 
     * @param appointments
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    protected static Collection<Appointment> readFromFile(File file
            , ObservableList<AppointmentGroup> appointmentGroups
            , Collection<Appointment> appointments)
            throws ParserConfigurationException, SAXException 
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = null;
        try {
            doc = builder.parse(file);
        } catch (IOException e) {
//            Main.log.log(Level.WARNING, "Appointment file not found: " + file);
            return null;
        }
        
        Map<String, String> appointmentAttributes;
        Map<String, String> rootAttributes = DataUtilities.getAttributes(doc.getFirstChild(), "repeatRules");
        List<Integer> keys = DataUtilities.myGetList(rootAttributes, "keys", "");
        Iterator<Integer> keyIterator = keys.iterator();
        
        NodeList appointmentNodeList = doc.getElementsByTagName("appointment");
        for (int appointmentNodeCounter=0; appointmentNodeCounter < appointmentNodeList.getLength(); appointmentNodeCounter++)
        {
            Node appointmentNode = appointmentNodeList.item(appointmentNodeCounter);
            if (appointmentNode.hasAttributes())
            {
                Integer expectedKey = keyIterator.next();
                appointmentAttributes = (HashMap<String, String>) DataUtilities.getAttributes(appointmentNode, "appointment");
                String appointmentName = DataUtilities.myGet(appointmentAttributes, "summary", file.toString());
                String errorMessage = ", file: " + file + " summary: " + appointmentName;
                Appointment anAppointment = AppointmentFactory.newAppointment()
                        .unmarshal(appointmentAttributes, expectedKey, errorMessage);
                Integer i = anAppointment.getAppointmentGroupIndex();
                anAppointment.setAppointmentGroup(appointmentGroups.get(i));
                appointments.add(anAppointment);
            }
        }
        return appointments;
    }
        
    /**
     * extracts this class's fields from map of attributes
     * @param myKey 
     */
    protected Appointment unmarshal(Map<String, String> appointmentAttributes, Integer expectedKey, String errorMessage)
    {
      super.unmarshal(appointmentAttributes, expectedKey, errorMessage);
      setEndLocalDateTime(LocalDateTime.parse(DataUtilities.myGet(appointmentAttributes,endLocalDateTime.getName(), errorMessage), Settings.DATE_FORMAT_AGENDA));
      setStartLocalDateTime(LocalDateTime.parse( DataUtilities.myGet(appointmentAttributes, startLocalDateTime.getName(), errorMessage), Settings.DATE_FORMAT_AGENDA));
      return this;
    }

    /**
     * Copy factory
     * Returns new Appointment object with all fields copied from input parameter myAppointment
     * 
     * @param appointment
     * @return
     * @throws CloneNotSupportedException 
     */
    public Appointment copyInto(Appointment appointment) {
        super.copyInto(appointment);
        appointment.setEndLocalDateTime(getEndLocalDateTime());
        appointment.setStartLocalDateTime(getStartLocalDateTime());
        return appointment;
    }
    
    /**
     *  Copy's current object's fields into passed parameter
     *  
     */
    public Repeat copyInto(Repeat repeat) {
        copyInto(repeat.getAppointmentData());
        repeat.setStartLocalDate(getStartLocalDateTime().toLocalDate());
        repeat.setStartLocalTime(getStartLocalDateTime().toLocalTime());
        repeat.setEndLocalTime(getEndLocalDateTime().toLocalTime());
        DayOfWeek d = getStartLocalDateTime().getDayOfWeek();
        repeat.setDayOfWeek(d, true);
        return repeat;
    }
   
    @Override   // requires checking object property and, if not null, checking of wrapped value
    public boolean equals(Object obj) {
        Appointment testObj = (Appointment) obj;

        return super.equals(obj) &&
                getEndLocalDateTime().equals(testObj.getEndLocalDateTime()) &&
                getStartLocalDateTime().equals(getStartLocalDateTime());
    }

}
