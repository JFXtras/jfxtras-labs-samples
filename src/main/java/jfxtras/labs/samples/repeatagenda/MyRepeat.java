package jfxtras.labs.samples.repeatagenda;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.AppointmentFactory;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.DataUtilities;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Settings;

public class MyRepeat extends Repeat {

    private static int nextKey = 0;

    /** Unique number identifying this Repeat object. */
    private Integer key;
    public Integer getKey() { return key; }
    void setKey(Integer value) { key = value; } 
    public MyRepeat withKey(Integer value) { setKey(value); return this; }
    public boolean hasKey() { return (getKey() != null); } // new Repeat has no key

    public MyRepeat() { }
    
    public MyRepeat(Repeat oldRepeat) {
        if (oldRepeat != null) {
            oldRepeat.copyInto(this);           
        }
    }
    
//    @Override
//    public boolean equals(Object obj) {
//        MyRepeat testObj = (MyRepeat) obj;
//        System.out.println( "MyRepeat equals ");
//        return super.equals(obj);
//    }
    
    /**
     * Reads from a XML file a collection of all repeat rules, adds them to repeats
     * @param appointmentGroups 
     * 
     * @param inputFile: File originating in the Setting class
     * @param inputFile: File originating in the Setting class
     * @return the collection of repeats, to be put into KarateData.appointmentRepeatMap
     * @throws TransformerException
     * @throws ParserConfigurationException
     */
    public static Collection<Repeat> readFromFile(Path inputFile
            , List<AppointmentGroup> appointmentGroups
            , Collection<Repeat> repeats) throws TransformerException, ParserConfigurationException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        try
        {
            Document doc = builder.parse(inputFile.toFile());
            Map<String, String> rootAttributes = DataUtilities.getAttributes(doc.getFirstChild(), "repeatRules");
            List<Integer> keys = DataUtilities.myGetList(rootAttributes, "keys", "");
            Iterator<Integer> keyIterator = keys.iterator();

            NodeList myNodeList = doc.getElementsByTagName("repeat");
            for (int n=0; n<myNodeList.getLength(); n++)
            {
                Node myNode = myNodeList.item(n);
                if (myNode.hasAttributes())
                {
                    try 
                    {
                        Integer myKey = keyIterator.next();
                        nextKey = Math.max(nextKey, myKey);
                        Repeat myRepeat = new MyRepeat().unmarshal((Element) myNodeList.item(n), myKey);
                        int i = ((MyAppointment) myRepeat.getAppointmentData()).getAppointmentGroupIndex();
//                        System.out.println("i " + i);
//                        Integer i = myRepeat.getAppointmentData().getAppointmentGroup().getKey();
//                        Integer i = myRepeat.getAppointmentData().getAppointmentGroup().;
                        myRepeat.getAppointmentData().setAppointmentGroup(appointmentGroups.get(i));
                        repeats.add(myRepeat);
                    } catch (IllegalArgumentException e2)
                    {
//                        Main.log.log(Level.WARNING, "Repeat rule skipped: " + inputFile.toString() + " key=" + keys.get(n), e2);                   
                    }
                }
            }
        } catch (SAXException | IOException e) {
//            Main.log.log(Level.WARNING, "Missing file: " + inputFile.toString(), e);
        }
        if (repeats.size() > 0) nextKey++;
//        if (repeats.size() > 0) {
//            nextKey = DataUtilities.checkNextKey(nextKey
//                    , repeats.stream().map(r -> r.getKey()).collect(Collectors.toSet())
//                    , "Repeat.nextRepeatKey");
//        }
        return repeats;
    }
    
    /**
     * Unmarshal one org.w3c.dom.Element into a new Repeat object
     * @param expectedKey 
     * 
     * @param myElement: Element with one Repeat object's data
     * @return A Repeat object with all the data fields filled from the Element
     */
    private MyRepeat unmarshal(Element myElement, Integer expectedKey)
    {
        Map<String, String> repeatAttributes = DataUtilities.getAttributes(myElement, "repeat");

        setKey(Integer.valueOf(DataUtilities.myGet(repeatAttributes, "key", "")));
        if (! (getKey() == expectedKey)) {
//            Main.log.log(Level.WARNING, "Repeat key does not match expected key. Repeat key = " + getKey()
//                    + " Expected repeat key = " + expectedKey + ". Using expected repeat key.", new IllegalArgumentException());
        }
        String intervalUnitString = DataUtilities.myGet(repeatAttributes, "intervalUnit", "");
        IntervalUnit myIntervalUnit = IntervalUnit.valueOf(intervalUnitString);
        setIntervalUnit(myIntervalUnit);
        setRepeatFrequency(DataUtilities.myParseInt(DataUtilities.myGet(repeatAttributes, "repeatFrequency", "")));
        String endCriteriaString = DataUtilities.myGet(repeatAttributes, "endCriteria", "");
        EndCriteria myEndCriteria = EndCriteria.valueOf(endCriteriaString);
        setEndCriteria(myEndCriteria);
        setStartLocalDate(DataUtilities.myParseLocalDate(DataUtilities.myGet(repeatAttributes, "startDate", "")));
        setStartLocalTime(DataUtilities.myParseLocalTime(DataUtilities.myGet(repeatAttributes, "startTime", ""), Settings.TIME_FORMAT_AGENDA));
        setEndLocalTime(DataUtilities.myParseLocalTime(DataUtilities.myGet(repeatAttributes, "endTime", ""), Settings.TIME_FORMAT_AGENDA));
        setDeletedDates(DataUtilities.myGetSet(repeatAttributes, "deletedDates", "", Settings.DATE_FORMAT1));

        switch (myIntervalUnit) {
            case DAILY:
                break;
            case WEEKLY:
                Arrays.stream(DataUtilities.myGet(repeatAttributes, "daysOfWeek", "").split(" "))
                      .map(a -> DayOfWeek.valueOf(a))
                      .forEach(a -> getDayOfWeekMap().get(a).set(true));
                break;
            case MONTHLY:
                setMonthlyRepeat(MonthlyRepeat.valueOf(DataUtilities.myGet(repeatAttributes, "monthlyRepeat", "")));
                break;
            case YEARLY:
                break;
            default:
                break;
        }

        switch (myEndCriteria) {
            case NEVER:
                break;
            case AFTER:
                setEndAfterEvents(DataUtilities.myParseInt(DataUtilities.myGet(repeatAttributes, "endAfterEvents", "")));
                // fall through
            case ON:
                setEndOnDate(LocalDate.parse(DataUtilities.myGet(repeatAttributes, "endOnDate", ""), Settings.DATE_FORMAT1));
                break;
            default:
                break;
        }
        
        Element appointmentElement = (Element) myElement.getElementsByTagName("appointment").item(0);   // must be only one appointment element
        Map<String, String> appointmentAttributes = DataUtilities.getAttributes(appointmentElement, "appointment");
        MyAppointment appointment = AppointmentFactory.newAppointment().unmarshal(appointmentAttributes, "Repeat appointment settings");
//        System.out.println("appointment.getAppointmentGroupIndex() " + appointment.getAppointmentGroupIndex());
        setAppointmentData(appointment);
//        int i = Integer.parseInt(DataUtilities.myGet(appointmentAttributes, "groupIndex", "Repeat appointment settings"));
// PROBLEM - I DON'T HAVE THE GROUPS HERE - HOW DO I CONNECT INDEX WITH GROUPS?
        //        AppointmentFactory.returnRepeatable(appointmentData).unmarshal(appointmentAttributes, "Repeat appointment settings");
        return this;
    }
    
    /**
     * Writes a set of repeat rules to a file.
     * 
     * @param appointmentRepeatMap: Map of all Repeat objects to be written (KarateDataUtilities.appointmentRepeatMap)
     * @param writeFile: File on disk for new data - overwrites automatically
     * @throws ParserConfigurationException
     */
    private static void writeToFile(Collection<Repeat> repeats, Path writeFile)
    {
        Set<MyRepeat> myRepeats = repeats
                .stream()
                .map(a -> (MyRepeat) a).collect(Collectors.toSet());
        
        // XML document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        }
        Document doc = builder.newDocument();

        // root node
        Element rootElement = doc.createElement("repeatRules");
        doc.appendChild(rootElement);

        // loop through each repeat
        for(MyRepeat myRepeat : myRepeats)
        {
            Node myElement = myRepeat.marshal(doc);
            rootElement.appendChild(myElement);
        }

        String repeatKeys = myRepeats
                .stream()
                .map(a -> a.getKey().toString()).collect(Collectors.joining(" "));
        rootElement.setAttribute("keys", repeatKeys);

        try {
            DataUtilities.writeDocument(doc, writeFile);
        } catch (TransformerException e) {
//              Main.log.log(Level.SEVERE, "Appointment Repeat file " + writeFile + " can't be written");
            e.printStackTrace();
        }
    }
    /**
     * Writes a map of repeat rules to a default file.
     * 
     * @param appointmentRepeatMap: Map of all Repeat objects to be written (KarateDataUtilities.appointmentRepeatMap)
     * @throws ParserConfigurationException
     */
    public static void writeToFile(Collection<Repeat> repeats)
    {
        Path appointmentRepeatsPath = Paths.get(Main.class.getResource("").getPath() + "appointmentRepeats.xml");
//        System.out.println(appointmentRepeatsPath);
        writeToFile(repeats, appointmentRepeatsPath);
    }
    
        
    /**
     * Marshal one Repeat object into one org.w3c.dom.Element
     * 
     * @param Document: org.w3c.dom.Document used to make a new element to be returned
     * @return: myElement populated with attributes containing Repeat object data
     */
    private Element marshal(Document doc)
    {
        Element myElement = doc.createElement("repeat");
        myElement.setAttribute("endCriteria", getEndCriteria().toString());
        myElement.setAttribute("intervalUnit", getIntervalUnit().toString());
        if (getKey() == null) setKey(nextKey++); // if it has no key (meaning its new) give it the next one
        myElement.setAttribute("key", Integer.toString(getKey()));
        myElement.setAttribute("repeatFrequency", Integer.toString(getRepeatFrequency()));
        myElement.setAttribute("startDate", DataUtilities.myFormatLocalDate(getStartLocalDate()));
        myElement.setAttribute("startTime", DataUtilities.myFormatLocalTime(getStartLocalTime()));
        myElement.setAttribute("endTime", DataUtilities.myFormatLocalTime(getEndLocalTime()));
        String d = getDeletedDates().stream()
                                    .map(a -> DataUtilities.myFormatLocalDate(a))
                                    .collect(Collectors.joining(" "));
        myElement.setAttribute("deletedDates", d);

        switch (getIntervalUnit())
        {
            case DAILY:
                break;
            case WEEKLY:
                String days = getDayOfWeekMap().entrySet()
                                                    .stream()
                                                    .filter(a -> a.getValue().get())
                                                    .map(a -> a.getKey().toString())
                                                    .collect(Collectors.joining(" "));
                myElement.setAttribute("daysOfWeek", days);
                break;
            case MONTHLY:
                myElement.setAttribute("monthlyRepeat", getMonthlyRepeat().toString());
                break;
            case YEARLY:
                break;
            default:
//                Main.log.log(Level.WARNING, "Unknown intervalUnit " + getIntervalUnit());
                break;
        }

        switch (getEndCriteria())
        {
            case NEVER:
                break;
            case AFTER:
                myElement.setAttribute("endAfterEvents", getEndAfterEvents().toString());
//                if (getEndOnDate() == null) makeEndOnDateFromEndAfterEvents();  // new AFTER repeat rules need end dates calculated.
                // fall through
            case ON:
                myElement.setAttribute("endOnDate", getEndOnDate().toString());
                break;
            default:
                break;
        }
        
        Element appointmentElement = doc.createElement("appointment");
        AppointmentFactory.returnConcreteAppointment(getAppointmentData()).marshal(appointmentElement);
//        getAppointmentData().marshal(appointmentElement);
        myElement.appendChild(appointmentElement);
        
        return myElement;
    }
}
