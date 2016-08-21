// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: model.proto at 132:1
package com.tevinjeffrey.rutgersct.data.uctapi.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.squareup.wire.AndroidMessage;
import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class Meta extends AndroidMessage<Meta, Meta.Builder> {
  public static final ProtoAdapter<Meta> ADAPTER = new ProtoAdapter_Meta();

  public static final Parcelable.Creator<Meta> CREATOR = AndroidMessage.newCreator(ADAPTER);

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_CODE = 0;

  public static final String DEFAULT_MESSAGE = "";

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#INT32"
  )
  @Nullable
  public final Integer code;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  @Nullable
  public final String message;

  public Meta(@Nullable Integer code, @Nullable String message) {
    this(code, message, ByteString.EMPTY);
  }

  public Meta(@Nullable Integer code, @Nullable String message, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.code = code;
    this.message = message;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.code = code;
    builder.message = message;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof Meta)) return false;
    Meta o = (Meta) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(code, o.code)
        && Internal.equals(message, o.message);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (code != null ? code.hashCode() : 0);
      result = result * 37 + (message != null ? message.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (code != null) builder.append(", code=").append(code);
    if (message != null) builder.append(", message=").append(message);
    return builder.replace(0, 2, "Meta{").append('}').toString();
  }

  public static final class Builder extends Message.Builder<Meta, Builder> {
    public Integer code;

    public String message;

    public Builder() {
    }

    public Builder code(Integer code) {
      this.code = code;
      return this;
    }

    public Builder message(String message) {
      this.message = message;
      return this;
    }

    @Override
    public Meta build() {
      return new Meta(code, message, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_Meta extends ProtoAdapter<Meta> {
    ProtoAdapter_Meta() {
      super(FieldEncoding.LENGTH_DELIMITED, Meta.class);
    }

    @Override
    public int encodedSize(Meta value) {
      return (value.code != null ? ProtoAdapter.INT32.encodedSizeWithTag(1, value.code) : 0)
          + (value.message != null ? ProtoAdapter.STRING.encodedSizeWithTag(2, value.message) : 0)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, Meta value) throws IOException {
      if (value.code != null) ProtoAdapter.INT32.encodeWithTag(writer, 1, value.code);
      if (value.message != null) ProtoAdapter.STRING.encodeWithTag(writer, 2, value.message);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public Meta decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.code(ProtoAdapter.INT32.decode(reader)); break;
          case 2: builder.message(ProtoAdapter.STRING.decode(reader)); break;
          default: {
            FieldEncoding fieldEncoding = reader.peekFieldEncoding();
            Object value = fieldEncoding.rawProtoAdapter().decode(reader);
            builder.addUnknownField(tag, fieldEncoding, value);
          }
        }
      }
      reader.endMessage(token);
      return builder.build();
    }

    @Override
    public Meta redact(Meta value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
