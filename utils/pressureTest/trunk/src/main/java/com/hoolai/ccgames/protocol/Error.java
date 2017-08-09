// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Error.proto

package com.hoolai.ccgames.protocol;

public final class Error {
  private Error() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registry.add(com.hoolai.ccgames.protocol.Error.ErrorMsg.cmd);
  }
  public interface ErrorMsgOrBuilder extends
      // @@protoc_insertion_point(interface_extends:Error.ErrorMsg)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>required int32 err_code = 1;</code>
     */
    boolean hasErrCode();
    /**
     * <code>required int32 err_code = 1;</code>
     */
    int getErrCode();

    /**
     * <code>optional string description = 2;</code>
     */
    boolean hasDescription();
    /**
     * <code>optional string description = 2;</code>
     */
    java.lang.String getDescription();
    /**
     * <code>optional string description = 2;</code>
     */
    com.google.protobuf.ByteString
        getDescriptionBytes();
  }
  /**
   * Protobuf type {@code Error.ErrorMsg}
   */
  public static final class ErrorMsg extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:Error.ErrorMsg)
      ErrorMsgOrBuilder {
    // Use ErrorMsg.newBuilder() to construct.
    private ErrorMsg(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private ErrorMsg(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final ErrorMsg defaultInstance;
    public static ErrorMsg getDefaultInstance() {
      return defaultInstance;
    }

    public ErrorMsg getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private ErrorMsg(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {
              bitField0_ |= 0x00000001;
              errCode_ = input.readInt32();
              break;
            }
            case 18: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000002;
              description_ = bs;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.hoolai.ccgames.protocol.Error.internal_static_Error_ErrorMsg_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.hoolai.ccgames.protocol.Error.internal_static_Error_ErrorMsg_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.hoolai.ccgames.protocol.Error.ErrorMsg.class, com.hoolai.ccgames.protocol.Error.ErrorMsg.Builder.class);
    }

    public static com.google.protobuf.Parser<ErrorMsg> PARSER =
        new com.google.protobuf.AbstractParser<ErrorMsg>() {
      public ErrorMsg parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new ErrorMsg(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<ErrorMsg> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int ERR_CODE_FIELD_NUMBER = 1;
    private int errCode_;
    /**
     * <code>required int32 err_code = 1;</code>
     */
    public boolean hasErrCode() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>required int32 err_code = 1;</code>
     */
    public int getErrCode() {
      return errCode_;
    }

    public static final int DESCRIPTION_FIELD_NUMBER = 2;
    private java.lang.Object description_;
    /**
     * <code>optional string description = 2;</code>
     */
    public boolean hasDescription() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional string description = 2;</code>
     */
    public java.lang.String getDescription() {
      java.lang.Object ref = description_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          description_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string description = 2;</code>
     */
    public com.google.protobuf.ByteString
        getDescriptionBytes() {
      java.lang.Object ref = description_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        description_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private void initFields() {
      errCode_ = 0;
      description_ = "";
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      if (!hasErrCode()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeInt32(1, errCode_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getDescriptionBytes());
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, errCode_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getDescriptionBytes());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.hoolai.ccgames.protocol.Error.ErrorMsg parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.hoolai.ccgames.protocol.Error.ErrorMsg parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.hoolai.ccgames.protocol.Error.ErrorMsg parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.hoolai.ccgames.protocol.Error.ErrorMsg parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.hoolai.ccgames.protocol.Error.ErrorMsg parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.hoolai.ccgames.protocol.Error.ErrorMsg parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.hoolai.ccgames.protocol.Error.ErrorMsg parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.hoolai.ccgames.protocol.Error.ErrorMsg parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.hoolai.ccgames.protocol.Error.ErrorMsg parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.hoolai.ccgames.protocol.Error.ErrorMsg parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.hoolai.ccgames.protocol.Error.ErrorMsg prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code Error.ErrorMsg}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:Error.ErrorMsg)
        com.hoolai.ccgames.protocol.Error.ErrorMsgOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.hoolai.ccgames.protocol.Error.internal_static_Error_ErrorMsg_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.hoolai.ccgames.protocol.Error.internal_static_Error_ErrorMsg_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.hoolai.ccgames.protocol.Error.ErrorMsg.class, com.hoolai.ccgames.protocol.Error.ErrorMsg.Builder.class);
      }

      // Construct using com.hoolai.ccgames.protocol.Error.ErrorMsg.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        errCode_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        description_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.hoolai.ccgames.protocol.Error.internal_static_Error_ErrorMsg_descriptor;
      }

      public com.hoolai.ccgames.protocol.Error.ErrorMsg getDefaultInstanceForType() {
        return com.hoolai.ccgames.protocol.Error.ErrorMsg.getDefaultInstance();
      }

      public com.hoolai.ccgames.protocol.Error.ErrorMsg build() {
        com.hoolai.ccgames.protocol.Error.ErrorMsg result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.hoolai.ccgames.protocol.Error.ErrorMsg buildPartial() {
        com.hoolai.ccgames.protocol.Error.ErrorMsg result = new com.hoolai.ccgames.protocol.Error.ErrorMsg(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.errCode_ = errCode_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.description_ = description_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.hoolai.ccgames.protocol.Error.ErrorMsg) {
          return mergeFrom((com.hoolai.ccgames.protocol.Error.ErrorMsg)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.hoolai.ccgames.protocol.Error.ErrorMsg other) {
        if (other == com.hoolai.ccgames.protocol.Error.ErrorMsg.getDefaultInstance()) return this;
        if (other.hasErrCode()) {
          setErrCode(other.getErrCode());
        }
        if (other.hasDescription()) {
          bitField0_ |= 0x00000002;
          description_ = other.description_;
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (!hasErrCode()) {
          
          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.hoolai.ccgames.protocol.Error.ErrorMsg parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.hoolai.ccgames.protocol.Error.ErrorMsg) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private int errCode_ ;
      /**
       * <code>required int32 err_code = 1;</code>
       */
      public boolean hasErrCode() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>required int32 err_code = 1;</code>
       */
      public int getErrCode() {
        return errCode_;
      }
      /**
       * <code>required int32 err_code = 1;</code>
       */
      public Builder setErrCode(int value) {
        bitField0_ |= 0x00000001;
        errCode_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required int32 err_code = 1;</code>
       */
      public Builder clearErrCode() {
        bitField0_ = (bitField0_ & ~0x00000001);
        errCode_ = 0;
        onChanged();
        return this;
      }

      private java.lang.Object description_ = "";
      /**
       * <code>optional string description = 2;</code>
       */
      public boolean hasDescription() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional string description = 2;</code>
       */
      public java.lang.String getDescription() {
        java.lang.Object ref = description_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            description_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string description = 2;</code>
       */
      public com.google.protobuf.ByteString
          getDescriptionBytes() {
        java.lang.Object ref = description_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          description_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string description = 2;</code>
       */
      public Builder setDescription(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        description_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string description = 2;</code>
       */
      public Builder clearDescription() {
        bitField0_ = (bitField0_ & ~0x00000002);
        description_ = getDefaultInstance().getDescription();
        onChanged();
        return this;
      }
      /**
       * <code>optional string description = 2;</code>
       */
      public Builder setDescriptionBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        description_ = value;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:Error.ErrorMsg)
    }

    static {
      defaultInstance = new ErrorMsg(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:Error.ErrorMsg)
    public static final int CMD_FIELD_NUMBER = 200;
    /**
     * <code>extend .Common.BaseCommand { ... }</code>
     */
    public static final
      com.google.protobuf.GeneratedMessage.GeneratedExtension<
        com.hoolai.ccgames.protocol.Command.BaseCommand,
        com.hoolai.ccgames.protocol.Error.ErrorMsg> cmd = com.google.protobuf.GeneratedMessage
            .newMessageScopedGeneratedExtension(
          com.hoolai.ccgames.protocol.Error.ErrorMsg.getDefaultInstance(),
          0,
          com.hoolai.ccgames.protocol.Error.ErrorMsg.class,
          com.hoolai.ccgames.protocol.Error.ErrorMsg.getDefaultInstance());
  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_Error_ErrorMsg_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_Error_ErrorMsg_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\013Error.proto\022\005Error\032\014Common.proto\"e\n\010Er" +
      "rorMsg\022\020\n\010err_code\030\001 \002(\005\022\023\n\013description\030" +
      "\002 \001(\t22\n\003cmd\022\023.Common.BaseCommand\030\310\001 \001(\013" +
      "2\017.Error.ErrorMsgB$\n\033com.hoolai.ccgames." +
      "protocolB\005Error"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.hoolai.ccgames.protocol.Command.getDescriptor(),
        }, assigner);
    internal_static_Error_ErrorMsg_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_Error_ErrorMsg_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_Error_ErrorMsg_descriptor,
        new java.lang.String[] { "ErrCode", "Description", });
    com.hoolai.ccgames.protocol.Command.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
