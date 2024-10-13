package xyz.srnyx.magicmongo.codecs;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;


/**
 * A {@link Codec} for encoding and decoding {@link UUID UUIDs} to and from {@link String Strings}
 */
public class UUIDCodec implements Codec<UUID> {
    @Override
    public void encode(@NotNull BsonWriter writer, @NotNull UUID value, EncoderContext encoderContext) {
        writer.writeString(value.toString());
    }

    @Override @NotNull
    public UUID decode(@NotNull BsonReader reader, DecoderContext decoderContext) {
        return UUID.fromString(reader.readString());
    }

    @Override
    public Class<UUID> getEncoderClass() {
        return UUID.class;
    }
}