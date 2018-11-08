/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/mrchen/Desktop/AndroidTest/app/src/main/aidl/com/AndroidAssistant/IMyAidlInterface.aidl
 */
package com.AndroidAssistant;
// Declare any non-default types here with import statements

public interface IMyAidlInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.AndroidAssistant.IMyAidlInterface
{
private static final java.lang.String DESCRIPTOR = "com.AndroidAssistant.IMyAidlInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.AndroidAssistant.IMyAidlInterface interface,
 * generating a proxy if needed.
 */
public static com.AndroidAssistant.IMyAidlInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.AndroidAssistant.IMyAidlInterface))) {
return ((com.AndroidAssistant.IMyAidlInterface)iin);
}
return new com.AndroidAssistant.IMyAidlInterface.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
java.lang.String descriptor = DESCRIPTOR;
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(descriptor);
return true;
}
case TRANSACTION_callCustomMethod:
{
data.enforceInterface(descriptor);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.callCustomMethod(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
default:
{
return super.onTransact(code, data, reply, flags);
}
}
}
private static class Proxy implements com.AndroidAssistant.IMyAidlInterface
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public boolean callCustomMethod(java.lang.String name) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(name);
mRemote.transact(Stub.TRANSACTION_callCustomMethod, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_callCustomMethod = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public boolean callCustomMethod(java.lang.String name) throws android.os.RemoteException;
}
