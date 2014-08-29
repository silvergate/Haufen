package com.dcrux.haufen;

import com.dcrux.haufen.impl.document.DocumentDeserializer;
import com.dcrux.haufen.impl.document.DocumentSerializer;
import com.dcrux.haufen.impl.document.IDocumentElement;
import com.dcrux.haufen.impl.document.elements.*;
import com.dcrux.haufen.impl.base.*;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * Created by caelis on 22/07/14.
 */
public class Test {

    private static IDocumentElement createDocument() {
        BagElement doc = new BagElement();

        MapElement obj = new MapElement();
        obj.put(new StringElement("age"), new StringElement("22"));
        obj.put(new StringElement("name"), new StringElement("Ruoss"));
        obj.put(new StringElement("firstName"), new StringElement("Simon"));
        //TODO: Make it work.
        obj.put(new StringElement("referenceToMyself"), obj);
        obj.put(new StringElement("aTrueBoolean"), new BooleanElement(true));
        obj.put(new StringElement("aFalseBoolean"), new BooleanElement(false));

        doc.add(new StringElement("Hallo Element Two"));
        doc.add(new StringElement("Another element"));
        doc.add(new StringElement("short"));
        doc.add(doc);
        doc.add(obj);
        doc.add(new StringElement("Hallo Element Two"));
        doc.add(new StringElement("llllllllllllllll ooooooonnnnng"));
        doc.add(new StringElement("Hallo Element One"));

        doc.add(new IntegerElement(223));

        return doc;
    }

    private static void showDocument(IDocumentElement documentElement) {
        System.out.println("Output: " + documentElement);
    }

    private static void readHead() {
        //bbbbbssa
        byte unmasked = -1;
        int value = unmasked & 255;
        System.out.println("valueAsInt:" + Integer.toBinaryString(value));
        byte baseType = (byte)(value >>> 3);
        System.out.println("bt as binar:" + Integer.toBinaryString(baseType));

        byte subtype = (byte)(value & 0b00000111 >>> 1);
        byte annotation = (byte)(value & 0b00000001);
        System.out.println("value:" + value + ", bt:" + baseType + ", subtype:" + subtype + ", annotation:" + annotation);
    }

    private static void writeHead() {
        //bbbbbssa
        byte baseType = 31;
        byte subtype = 3;
        byte annotation = 1;

        int value = 0;
        value = value | (annotation);
        value = value | (subtype << 1);
        value = value | (baseType << 3);

        System.out.println("value:" + value);
        System.out.println("value:" + (byte)value);

    }

    public static void main(String[] fsd) throws Exception {
        writeHead();
        readHead();

        /*BigDecimal bd1 = new BigDecimal(new BigInteger("-0"), 0);
        BigDecimal bd2 = new BigDecimal(new BigInteger("-0"), -1);

        System.out.println(bd1);
        System.out.println(bd2);
        System.out.println(bd1.doubleValue() + " " + bd2.doubleValue());*/


        /* Serialize */
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputImpl dataOutput = new DataOutputImpl(baos);

        Serializer serializer = new Serializer();
        serializer.setOutput(dataOutput);

        DocumentSerializer documentSerializer = new DocumentSerializer();
        documentSerializer.serialize(serializer, createDocument());

        System.out.println("Length: " + baos.toByteArray().length);
        System.out.println(Arrays.toString(baos.toByteArray()));

        /* Deserialize */
        DataInputImpl dataInput = new DataInputImpl(baos.toByteArray());

        Deserializer deserializer = new Deserializer();
        deserializer.setDataInput(dataInput);

        DocumentDeserializer documentDeserializer = new DocumentDeserializer();
        IDocumentElement newDocument = documentDeserializer.deserialize(deserializer);
        showDocument(newDocument);
    }
}
