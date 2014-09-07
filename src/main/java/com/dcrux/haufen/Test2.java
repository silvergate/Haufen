package com.dcrux.haufen;

import com.dcrux.haufen.impl.base.DataInputImpl;
import com.dcrux.haufen.impl.base.DataOutputImpl;
import com.dcrux.haufen.newimpl.IElement;
import com.dcrux.haufen.newimpl.elements.bag.BagElement;
import com.dcrux.haufen.newimpl.elements.bool.BoolElement;
import com.dcrux.haufen.newimpl.elements.integer.IntegerElement;
import com.dcrux.haufen.newimpl.elements.map.MapElement;
import com.dcrux.haufen.newimpl.elements.string.StringElement;
import com.dcrux.haufen.newimpl.serializer.Deserializer;
import com.dcrux.haufen.newimpl.serializer.Serializer;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * Created by caelis on 22/07/14.
 */
public class Test2 {


    public static void main(String[] fsd) throws Exception {
        /* Serialize */
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputImpl dataOutput = new DataOutputImpl(baos);

        /* Document */
        StringElement str1 = new StringElement("ABC_Dies_Ist_Ein_Text_BCA");
        IntegerElement i1 = new IntegerElement(43);
        IntegerElement i2 = new IntegerElement(43);
        IntegerElement i3 = new IntegerElement(43);

        MapElement me1 = new MapElement();
        me1.put(new StringElement("age"), new IntegerElement(21));
        me1.put(new StringElement("firstName"), new StringElement("Simon"));
        me1.put(new StringElement("male"), new BoolElement());

        BagElement bagElement1 = new BagElement();
        bagElement1.add(str1);
        bagElement1.add(i1);
        bagElement1.add(i2);
        bagElement1.add(i3);
        bagElement1.add(i3);
        bagElement1.add(me1);

        Serializer serializer = new Serializer();
        serializer.serialize(dataOutput, bagElement1);

        System.out.println("Length: " + baos.toByteArray().length);
        System.out.println(Arrays.toString(baos.toByteArray()));

         /* Deserialize */
        DataInputImpl dataInput = new DataInputImpl(baos.toByteArray());
        Deserializer deserializer = new Deserializer();
        IElement element = deserializer.deserialize(dataInput);

        System.out.println("Deserialized: '" + element + "'");
    }
}
