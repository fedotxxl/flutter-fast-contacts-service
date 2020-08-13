import Flutter
import UIKit
import Contacts
import ContactsUI

@available(iOS 9.0, *)
public class SwiftContactsServicePlugin: NSObject, FlutterPlugin {
    private var result: FlutterResult? = nil
    static let FORM_OPERATION_CANCELED: Int = 1
    static let FORM_COULD_NOT_BE_OPEN: Int = 2

    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "fast_contacts_service_method_channel", binaryMessenger: registrar.messenger())
        let instance = SwiftContactsServicePlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }

    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        switch call.method {
        case "listContacts":
            let arguments = call.arguments as! [String:Any]
            result(listContacts(localizedLabels: arguments["iOSLocalizedLabels"] as! Bool ))
        default:
            result(FlutterMethodNotImplemented)
        }
    }

    func listContacts(localizedLabels: Bool) -> [[String:Any]]{

        var contacts : [CNContact] = []
        var result = [[String:Any]]()

        //Create the store, keys & fetch request
        let store = CNContactStore()
        var keys = [CNContactFormatter.descriptorForRequiredKeys(for: .fullName),
                    CNContactEmailAddressesKey,
                    CNContactPhoneNumbersKey,
                    CNContactFamilyNameKey,
                    CNContactGivenNameKey,
                    CNContactMiddleNameKey,
                    CNContactNamePrefixKey,
                    CNContactNameSuffixKey] as [Any]


        let fetchRequest = CNContactFetchRequest(keysToFetch: keys as! [CNKeyDescriptor])

        // Fetch contacts
        do{
            try store.enumerateContacts(with: fetchRequest, usingBlock: { (contact, stop) -> Void in
                contacts.append(contact)
            })
        }
        catch let error as NSError {
            print(error.localizedDescription)
            return result
        }


        // Transform the CNContacts into dictionaries
        for contact : CNContact in contacts{
            result.append(contactToDictionary(contact: contact, localizedLabels: localizedLabels))
        }

        return result
    }

    func contactToDictionary(contact: CNContact, localizedLabels: Bool) -> [String:Any]{

        var result = [String:Any]()

        //Simple fields
        result["identifier"] = contact.identifier
        result["displayName"] = CNContactFormatter.string(from: contact, style: CNContactFormatterStyle.fullName)
        result["givenName"] = contact.givenName
        result["familyName"] = contact.familyName
        result["middleName"] = contact.middleName
        result["prefix"] = contact.namePrefix
        result["suffix"] = contact.nameSuffix

        //Phone numbers
        var phoneNumbers = [[String:String]]()
        for phone in contact.phoneNumbers{
            var phoneDictionary = [String:String]()
            phoneDictionary["value"] = phone.value.stringValue
            phoneDictionary["label"] = "other"
            if let label = phone.label{
                phoneDictionary["label"] = localizedLabels ? CNLabeledValue<NSString>.localizedString(forLabel: label) : getRawPhoneLabel(label);
            }
            phoneNumbers.append(phoneDictionary)
        }
        result["phones"] = phoneNumbers

        //Emails
        var emailAddresses = [[String:String]]()
        for email in contact.emailAddresses{
            var emailDictionary = [String:String]()
            emailDictionary["value"] = String(email.value)
            emailDictionary["label"] = "other"
            if let label = email.label{
                emailDictionary["label"] = localizedLabels ? CNLabeledValue<NSString>.localizedString(forLabel: label) : getRawCommonLabel(label);
            }
            emailAddresses.append(emailDictionary)
        }
        result["emails"] = emailAddresses

        return result
    }

    func getRawPhoneLabel(_ label: String?) -> String{
        let labelValue = label ?? ""
        switch(labelValue){
            case CNLabelPhoneNumberMain: return "main"
            case CNLabelPhoneNumberMobile: return "mobile"
            case CNLabelPhoneNumberiPhone: return "iPhone"
            case CNLabelWork: return "work"
            case CNLabelHome: return "home"
            case CNLabelOther: return "other"
            default: return labelValue
        }
    }

    func getRawCommonLabel(_ label: String?) -> String{
        let labelValue = label ?? ""
        switch(labelValue){
            case CNLabelWork: return "work"
            case CNLabelHome: return "home"
            case CNLabelOther: return "other"
            default: return labelValue
        }
    }

}
