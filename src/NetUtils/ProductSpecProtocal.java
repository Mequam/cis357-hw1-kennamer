package CashProgram.NetUtils;

import CashProgram.CashItems.ProductSpecification;

import java.io.IOException;
import java.io.InputStream;

/**
 * this class contains all of the abstract mapping from
 * bytes to logical commands translated from the network
 *
 * basically, this is the one stop location to find functions
 * that translate between local representation of data and
 * network representation of data for ProductSpecification
 *
 * */
public class ProductSpecProtocal {
    /**
     * list of operations that can be performed on a product specification server
     * */
    public static enum COMMANDS {
        GET,
        UPDATE,
        DELETE,
        NOOP, //indicates no operation, used as an error code
        SPEC_NOT_FOUND //indicates that the given specification is not located
    }
    /**
     * since COMMANDS.values() generates a new array on every call,
     * we generate it ONE time, and use this as a reference moving forwards
     * */
    public static final COMMANDS[] COMMAND_VALUES = COMMANDS.values();



    /**
     * generates a new item_not_found_alert packet to send of to the
     * client
     * */
    public static byte [] gen_item_not_found_alert() {
        return new byte[] {(byte) COMMANDS.SPEC_NOT_FOUND.ordinal()};
    }
    /**
     * generates a packet indicating the product specification
     * was located and the specification of the product
     * */
    public static byte [] gen_product_spec_packet(ProductSpecification p) throws IOException {
        return tag_bytes(COMMANDS.UPDATE,p.encode());
    }
    /**
     * gets a command from the given bytes read in via the network
     * */
    public static COMMANDS get_command(byte [] data) {
        if (data.length > 0 && Byte.toUnsignedInt(data[0]) < data.length) {
            return COMMAND_VALUES[Byte.toUnsignedInt(data[0])];
        }
        return COMMANDS.NOOP;
    }

    /**
     * slices an array of bytes to the end of the array from the start
     * */
    public static byte [] slice_bytes(byte [] toSlice, int start) {
        return  slice_bytes(toSlice,start,toSlice.length - start);
    }
    /**
     * slices an array of bytes from the given start to the given length
     * */
    public static byte [] slice_bytes(byte [] toSlice , int start,int length) {
        byte [] ret_val = new byte[length];
        for (int i = 0 ; i < length; i++) {
            ret_val[i] = toSlice[i+start];
        }
        return ret_val;
    }

    /**
     * (probably) safely reads in the product specification bytes from the given input stream
     * this assumes we will be receiving an UPDATE packet containing the item code
     * */
    public byte[] read_specification_bytes(InputStream is) throws IOException {
        byte[] ret_val = new byte[ProductSpecification.maxEncodingSize()];

        //read in the header
        int headerLen = ProductSpecification.ItemCode.encoding_size + Double.BYTES + 1;
        System.arraycopy(is.readNBytes(headerLen),0,ret_val,0,headerLen);

        //read in the name from the header
        int nameLen = Byte.toUnsignedInt(ret_val[headerLen-1]);
        System.arraycopy(is.readNBytes(nameLen),0,ret_val,headerLen,nameLen);

        return slice_bytes(ret_val,0,nameLen+headerLen);
    }
    /**
     * takes a given encoding, and attaches the command to that encoding
     * */
    public static byte [] tag_bytes(COMMANDS c,byte [] bytes) {
        byte [] ret_val = new byte[bytes.length + 1];
        ret_val[0] = (byte)c.ordinal();
        System.arraycopy(bytes,0,ret_val,1,bytes.length);
        return ret_val;
    }
    /**
     * gets a new item code from a network packet
     * throws an error if the item code is invalid
     * */
    public static ProductSpecification.ItemCode get_item_code(byte [] commandBytes) throws ProductSpecification.ItemCode.MalformedItemCodeException {
        //I very much miss c++ pointers for this kind of thing
        return new ProductSpecification.ItemCode(
                slice_bytes(commandBytes,
                            1,
                                ProductSpecification.ItemCode.encoding_size)
        );
    }

    /**
     * generates a new get request that indicates the client wishes to
     * retrive an item code
     * */
    public static byte [] gen_get_request(ProductSpecification.ItemCode ic) {
            return tag_bytes(COMMANDS.GET,ic.encode());
    }
    public static int COMMAND_BYTE_SIZE;

    /**
     *
     * */
    public static byte [] readItemCode(InputStream is) {


        byte[] ret_val = new byte[COMMAND_BYTE_SIZE];
        for (int i = 0; i < ret_val.length;i++) {

        }
        return ret_val;
    }
    /**
     * returns a product specification from a modification packet
     * */
    public static ProductSpecification get_specification(byte [] packet) {
        return new ProductSpecification(slice_bytes(packet,1));
    }
}
