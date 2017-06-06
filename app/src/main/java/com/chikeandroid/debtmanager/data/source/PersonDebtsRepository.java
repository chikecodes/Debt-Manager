package com.chikeandroid.debtmanager.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chikeandroid.debtmanager.data.Debt;
import com.chikeandroid.debtmanager.data.Payment;
import com.chikeandroid.debtmanager.data.Person;
import com.chikeandroid.debtmanager.data.PersonDebt;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Chike on 3/24/2017.
 * Concrete implementation to load PersonDebts from the data sources into a cache.
 */
@Singleton
public class PersonDebtsRepository implements PersonDebtsDataSource {

    private final PersonDebtsDataSource mDebtsLocalDataSource;

    // private final DebtsDataSource mDebtsRemoteDataSource;

    private final List<DebtsRepositoryObserver> mObservers = new ArrayList<>();

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, PersonDebt> mCacheOweMePersonDebts;
    Map<String, PersonDebt> mCacheIOwePersonDebts;
    Map<String, Person> mCachePersons;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheOweMePersonDebtsIsDirty;
    boolean mCacheIOwePersonDebtsIsDirty;
    boolean mCachePersonIsDirty;

    @Inject
    public PersonDebtsRepository(@Local PersonDebtsDataSource debtsLocalDataSource) {
        mDebtsLocalDataSource = debtsLocalDataSource;
    }

    /**
     * Adds observer to be notify of data changes
     * @param observer the observer
     */
    public void addContentObserver(DebtsRepositoryObserver observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    /**
     * Removes an observer
     * @param observer the observer to remove
     */
    public void removeContentObserver(DebtsRepositoryObserver observer) {
        if (mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    /**
     * Notify all observers based on the debt type
     * @param debtType the debt type
     */
    private void notifyContentObserver(int debtType) {
        for (DebtsRepositoryObserver observer : mObservers) {
            observer.onDebtsChanged(debtType);
        }
    }

    /**
     * Checks if owe me debts cache is available
     * @return either true or false
     */
    public boolean cachedOweMePersonDebtsAvailable() {
        return mCacheOweMePersonDebts != null && !mCacheOweMePersonDebtsIsDirty;
    }

    /**
     * Checks if i owe debts cache is available
     * @return either true of false
     */
    public boolean cachedIOwePersonDebtsAvailable() {
        return mCacheIOwePersonDebts != null && !mCacheIOwePersonDebtsIsDirty;
    }

    /**
     * Checks if people cache is available
     * @return either true or false
     */
    public boolean cachedPeopleAvailable() {
        return mCachePersons != null && !mCachePersonIsDirty;
    }

    /**
     * Returns a List of Owe me {@link PersonDebt}s from cache
     * @return a List of {@link PersonDebt}s
     */
    public List<PersonDebt> getCacheOweMePersonDebts() {
        return mCacheOweMePersonDebts == null ? null : new ArrayList<>(mCacheOweMePersonDebts.values());
    }

    /**
     * Returns a List of I owe {@link PersonDebt}s from cache
     * @return a List of {@link PersonDebt}s
     */
    public List<PersonDebt> getCacheIOwePersonDebts() {
        return mCacheIOwePersonDebts == null ? null : new ArrayList<>(mCacheIOwePersonDebts.values());
    }

    /**
     * Returns a List of {@link Person}s from cache
     * @return a List of {@link Person}s
     */
    public List<Person> getCachePersons() {
        return mCachePersons == null ? null : new ArrayList<>(mCachePersons.values());
    }

    /**
     * Gets a Owe me {@link PersonDebt} from cache
     * @param debtId is the debt id
     * @return a {@link PersonDebt}
     */
    public PersonDebt getCachedOweMePersonDebt(String debtId) {
        return mCacheOweMePersonDebts.get(debtId) == null ? null : mCacheOweMePersonDebts.get(debtId);
    }

    /**
     * Gets a I owe {@link PersonDebt} from cache
     * @param debtId is the debt id
     * @return a {@link PersonDebt}
     */
    public PersonDebt getCachedIOweDebt(String debtId) {
        return mCacheIOwePersonDebts.get(debtId) == null ? null : mCacheIOwePersonDebts.get(debtId);
    }

    /**
     * Get a {@link Person} debts from either cache or local database
     * @param personPhoneNumber the person id
     * @return a List of {@link Debt} object
     */
    @Override
    public List<Debt> getPersonDebts(String personPhoneNumber) {

        List<Debt> personDebts = new ArrayList<>();

        if (mCacheIOwePersonDebts != null) {
            for (Map.Entry<String, PersonDebt> entry : mCacheIOwePersonDebts.entrySet()) {
                if (entry.getValue().getPerson().getPhoneNumber().equals(personPhoneNumber)) {
                    personDebts.add(entry.getValue().getDebt());
                }
            }
        }

        if (mCacheOweMePersonDebts != null) {
            for (Map.Entry<String, PersonDebt> entry : mCacheOweMePersonDebts.entrySet()) {
                if (entry.getValue().getPerson().getPhoneNumber().equals(personPhoneNumber)) {
                    personDebts.add(entry.getValue().getDebt());
                }
            }
        }

        if (!personDebts.isEmpty()) {
            return personDebts;
        }

        return mDebtsLocalDataSource.getPersonDebts(personPhoneNumber);
    }

    /**
     * Get a {@link Debt} from either cache or local database
     * @param debtId the debt id
     * @return a {@link Debt} object
     */
    @Override
    public Debt getDebt(@NonNull String debtId) {

        if (getCachedOweMePersonDebt(debtId) != null) {
            return getCachedOweMePersonDebt(debtId).getDebt();
        }else if (getCachedIOweDebt(debtId) != null) {
            return  getCachedIOweDebt(debtId).getDebt();
        }

        return mDebtsLocalDataSource.getDebt(debtId);
    }

    /**
     * Deletes a {@link Payment} from local database and cache
     * @param payment the Payment object to be deleted
     */
    @Override
    public void deletePayment(@NonNull Payment payment) {

        mDebtsLocalDataSource.deletePayment(payment);

        if (payment.getAction() == Payment.PAYMENT_ACTION_DEBT_DECREASE) {
            double newDebtAmount = getDebt(payment.getDebtId()).getAmount() + payment.getAmount();
            setDebtAmount(payment.getDebtId(), newDebtAmount);
        } else if (payment.getAction() == Payment.PAYMENT_ACTION_DEBT_INCREASE) {
            double newDebtAmount = getDebt(payment.getDebtId()).getAmount() -  payment.getAmount();
            setDebtAmount(payment.getDebtId(), newDebtAmount);
        }

        Debt debt = getDebt(payment.getDebtId());

        if (debt.getDebtType() == Debt.DEBT_TYPE_IOWE && mCacheIOwePersonDebts != null) {
            mCacheIOwePersonDebts.get(debt.getId()).getPayments().remove(payment);
        }else if (debt.getDebtType() == Debt.DEBT_TYPE_OWED && mCacheOweMePersonDebts != null) {
            mCacheOweMePersonDebts.get(debt.getId()).getPayments().remove(payment);
        }
    }

    /**
     * Get a debt {@link Payment}s  from either database or cache
     * @param debtId the debt id
     * @return a List of {@link Payment}
     */
    @Override
    public List<Payment> getDebtPayments(@NonNull String debtId) {

        List<Payment> payments;

        if (cachedIOwePersonDebtsAvailable() && getCachedOweMePersonDebt(debtId) != null) {
            return mCacheOweMePersonDebts.get(debtId).getPayments();
        }else if (cachedIOwePersonDebtsAvailable() && getCachedIOweDebt(debtId) != null) {
            return mCacheIOwePersonDebts.get(debtId).getPayments();
        }

        payments = mDebtsLocalDataSource.getDebtPayments(debtId);

        processLoadedDebtPayment(payments, debtId);
        return payments;
    }

    /**
     * Saves the loaded debt Payments to cache
     * @param payments the Payments to be saved
     * @param debtId the debt id
     */
    private void processLoadedDebtPayment(List<Payment> payments, String debtId) {

        if (payments == null) {
            return;
        }

        if (cachedOweMePersonDebtsAvailable() && mCacheOweMePersonDebts.get(debtId) != null) {
            mCacheOweMePersonDebts.get(debtId).getPayments().clear();
            for (Payment payment : payments) {
                mCacheOweMePersonDebts.get(debtId).addPayment(payment);
            }
        }else if (cachedIOwePersonDebtsAvailable() && mCacheIOwePersonDebts.get(debtId) != null) {
            mCacheIOwePersonDebts.get(debtId).getPayments().clear();
            for (Payment payment : payments) {
                mCacheIOwePersonDebts.get(debtId).addPayment(payment);
            }
        }
    }

    /**
     * Deletes all Payments made to debt
     * @param debtId the debt id
     */
    @Override
    public void deleteAllDebtPayments(@NonNull String debtId) {
        if (mCacheOweMePersonDebts.get(debtId) != null) {
            mCacheOweMePersonDebts.get(debtId).getPayments().clear();
        }else if (mCacheIOwePersonDebts.get(debtId) != null) {
            mCacheIOwePersonDebts.get(debtId).getPayments().clear();
        }
    }

    /**
     * Saves a {@link Payment} object to local database and cache. This also updates the Payment's
     * Debt based on the Payment action selected.
     * @param payment the Payment object to be saved
     */
    @Override
    public void savePayment(@NonNull Payment payment) {
        checkNotNull(payment);

        mDebtsLocalDataSource.savePayment(payment);
        if (cachedIOwePersonDebtsAvailable() && getCachedIOweDebt(payment.getDebtId()) != null) {
            mCacheIOwePersonDebts.get(payment.getDebtId()).addPayment(payment);
        }else if (cachedOweMePersonDebtsAvailable() && getCachedOweMePersonDebt(payment.getDebtId()) != null) {
            mCacheOweMePersonDebts.get(payment.getDebtId()).addPayment(payment);
        }
        if (payment.getAction() == Payment.PAYMENT_ACTION_DEBT_DECREASE) {
            double newAmount = getDebt(payment.getDebtId()).getAmount() - payment.getAmount();
            setPaymentDebtAmount(payment, newAmount);
            setDebtAmount(payment.getDebtId(), newAmount);
        } else if (payment.getAction() == Payment.PAYMENT_ACTION_DEBT_INCREASE) {
            double newAmount = getDebt(payment.getDebtId()).getAmount() + payment.getAmount();
            setPaymentDebtAmount(payment, newAmount);
            setDebtAmount(payment.getDebtId(), newAmount);
        }
    }

    /**
     * Sets a Payment's debt amount
     * @param payment the Payment object
     * @param newAmount the new amount for the Payment's debt
     */
    private void setPaymentDebtAmount(@NonNull Payment payment, double newAmount) {
        Debt debt = getDebt(payment.getDebtId());
        List<Debt> personDebts = getPersonDebts(debt.getPersonPhoneNumber());
        int indexDebt = personDebts.indexOf(debt);
        personDebts.get(indexDebt).setAmount(newAmount);
    }

    /**
     * Updates a {@link Payment} in local database and also in cache. This also updates the Payment
     * Debt amount based on the Payment action selected
     * @param payment the updated payment
     * @param debt the payment's debt
     */
    @Override
    public void editPayment(@NonNull Payment payment, @NonNull Debt debt) {

        mDebtsLocalDataSource.editPayment(payment, debt);

        // update cache;
        List<Payment> debtPayments = new ArrayList<>();
        if (debt.getDebtType() == Debt.DEBT_TYPE_IOWE && mCacheIOwePersonDebts != null) {
            debtPayments = mCacheIOwePersonDebts.get(debt.getId()).getPayments();
        }else if (debt.getDebtType() == Debt.DEBT_TYPE_OWED && mCacheOweMePersonDebts != null) {
            debtPayments = mCacheOweMePersonDebts.get(debt.getId()).getPayments();
        }

        Payment initialPayment = null;
        for (Payment payment1 : debtPayments) {
            if (payment.getId().equals(payment1.getId())) {
                initialPayment = payment1;
                break;
            }
        }

        if (initialPayment != null) {
            double initialPaymentAmount = initialPayment.getAmount();
            double initialDebtAmount = getDebt(payment.getDebtId()).getAmount();

            if (initialPayment.getAction() == Payment.PAYMENT_ACTION_DEBT_DECREASE) {
                double amountResult = initialPaymentAmount + initialDebtAmount;
                double newDebtAmount = amountResult - payment.getAmount();
                setDebtAmount(payment.getDebtId(), newDebtAmount);
            } else if (initialPayment.getAction() == Payment.PAYMENT_ACTION_DEBT_INCREASE) {
                double amountResult = initialDebtAmount - initialPaymentAmount;
                double newDebtAmount = amountResult + payment.getAmount();
                setDebtAmount(payment.getDebtId(), newDebtAmount);
            }

            int index = debtPayments.indexOf(initialPayment);
            debtPayments.set(index, payment);
        }
    }

    /**
     * Sets a new debt amount to the debt
     * @param debtId the debt id
     * @param amount the new amount for the debt
     */
    private void setDebtAmount(@NonNull String debtId, double amount) {
        getDebt(debtId).setAmount(amount);
    }

    /**
     * Returns a {@link Payment} object from either cache or local database
     * @param paymentId the payment id
     * @param debt the Payment's debt
     * @return a {@link Payment}
     */
    @Override
    public Payment getPayment(@NonNull String paymentId, @NonNull Debt debt) {

        List<Payment> debtPayments = new ArrayList<>();

        if (debt.getDebtType() == Debt.DEBT_TYPE_IOWE && mCacheIOwePersonDebts.get(debt.getId()) != null) {
            debtPayments = mCacheIOwePersonDebts.get(debt.getId()).getPayments();
        }else if (debt.getDebtType() == Debt.DEBT_TYPE_OWED && mCacheOweMePersonDebts.get(debt.getId()) != null) {
            debtPayments = mCacheOweMePersonDebts.get(debt.getId()).getPayments();
        }

        if (!debtPayments.isEmpty()) {
            for (Payment payment : debtPayments) {
                if (payment.getId().equals(paymentId)) {
                    return payment;
                }
            }
        }

        return mDebtsLocalDataSource.getPayment(paymentId, debt);
    }

    /**
     * Deletes all debt payments from local database and cache
     */
    @Override
    public void deleteAllPayments() {
        mDebtsLocalDataSource.deleteAllPayments();
        if (mCacheIOwePersonDebts != null) {
            for (PersonDebt personDebt : getCacheIOwePersonDebts()) {
                mCacheIOwePersonDebts.get(personDebt.getDebt().getId()).getPayments().clear();
            }
        }

        if (mCacheOweMePersonDebts != null) {
            for (PersonDebt personDebt : getCacheOweMePersonDebts()) {
                mCacheOweMePersonDebts.get(personDebt.getDebt().getId()).getPayments().clear();
            }
        }
    }

    /**
     * Returns a {@link PersonDebt} from local cache or local database
     * @param debtId the debt id
     * @param debtType the debt type
     * @return a {@link PersonDebt} object
     */
    @Override
    public PersonDebt getPersonDebt(@NonNull String debtId, @NonNull int debtType) {
        checkNotNull(debtId);
        checkNotNull(debtType);

        PersonDebt cachedPersonDebt = null;
        if (debtType == Debt.DEBT_TYPE_OWED) {
            cachedPersonDebt = getOweMePersonDebtById(debtId);
        }else if (debtType == Debt.DEBT_TYPE_IOWE) {
            cachedPersonDebt = getDebtIOweById(debtId);
        }

        // Respond immediately with cache if we have one
        if (cachedPersonDebt != null) {
            return cachedPersonDebt;
        }

        return mDebtsLocalDataSource.getPersonDebt(debtId, debtType);
    }

    /**
     * Gets a owe me {@link PersonDebt} from cache
     * @param debtId is the debt id
     * @return a {@link PersonDebt}
     */
    @Nullable
    private PersonDebt getOweMePersonDebtById(@NonNull String debtId) {
        checkNotNull(debtId);
        if (mCacheOweMePersonDebts == null || mCacheOweMePersonDebts.isEmpty()) {
            return null;
        }else {
            return mCacheOweMePersonDebts.get(debtId);
        }
    }

    /**
     * Gets a I owe {@link PersonDebt} from cache
     * @param debtId is the debt id
     * @return a {@link PersonDebt}
     */
    @Nullable
    private PersonDebt getDebtIOweById(@NonNull String debtId) {
        checkNotNull(debtId);
        if (mCacheIOwePersonDebts == null || mCacheIOwePersonDebts.isEmpty()) {
            return null;
        }else {
            return mCacheIOwePersonDebts.get(debtId);
        }
    }

    /**
     * Returns a {@link Person} from cache
     * @param phoneNumber is the person phone number
     * @return a {@link Person}
     */
    @Nullable
    public Person getPerson(@NonNull String phoneNumber) {
        checkNotNull(phoneNumber);
        if (mCachePersons == null || mCachePersons.isEmpty()) {
            return null;
        }else {
            return mCachePersons.get(phoneNumber);
        }
    }

    /**
     * Gets all {@link PersonDebt}s from local database
     * @param debtType the debt type
     * @return a List of {@link PersonDebt}
     */
    @Override
    public List<PersonDebt> getAllPersonDebtsByType(@NonNull int debtType) {
        checkNotNull(debtType);

        List<PersonDebt> personDebts = null;
        if (debtType == Debt.DEBT_TYPE_OWED) {
            if (!mCacheOweMePersonDebtsIsDirty && mCacheOweMePersonDebts == null) {
                    personDebts = mDebtsLocalDataSource.getAllPersonDebtsByType(Debt.DEBT_TYPE_OWED);
            }

            if (!mCacheOweMePersonDebtsIsDirty && mCacheOweMePersonDebts != null) {
                personDebts = new ArrayList<>();
                for (PersonDebt personDebt : getCacheOweMePersonDebts()) {

                    Person person = new Person(personDebt.getPerson());
                    Debt debt = new Debt(personDebt.getDebt());

                    PersonDebt newPersonDebt = new PersonDebt(person, debt);
                    personDebts.add(newPersonDebt);
                }
                return personDebts;
            }
        }else if (debtType == Debt.DEBT_TYPE_IOWE) {
            if (!mCacheIOwePersonDebtsIsDirty && mCacheIOwePersonDebts == null) {
                personDebts = mDebtsLocalDataSource.getAllPersonDebtsByType(Debt.DEBT_TYPE_IOWE);
            }

            if (!mCacheIOwePersonDebtsIsDirty && mCacheIOwePersonDebts != null) {
                personDebts = new ArrayList<>();
                for (PersonDebt personDebt : getCacheIOwePersonDebts()) {
                    Person person = new Person(personDebt.getPerson());
                    Debt debt = new Debt(personDebt.getDebt());

                    PersonDebt newPersonDebt = new PersonDebt(person, debt);
                    personDebts.add(newPersonDebt);
                }
                return personDebts;
            }
        }

        processLoadedDebts(personDebts, debtType);

        return personDebts;
    }

    /**
     * Saves the loaded {@link PersonDebt}s in cache
     * @param personDebts the loaded PersonDebts object
     * @param debtType the debt type of the loaded PersonDebts
     */
    private void processLoadedDebts(List<PersonDebt> personDebts, @NonNull int debtType) {

        if (personDebts == null) {
            mCacheOweMePersonDebts = null;
            mCacheOweMePersonDebtsIsDirty = false;
            return;
        }

        if (debtType == Debt.DEBT_TYPE_OWED) {
            if (mCacheOweMePersonDebts == null) {
                mCacheOweMePersonDebts = new LinkedHashMap<>();
            }
            mCacheOweMePersonDebts.clear();

            for (PersonDebt personDebt : personDebts) {

                Person person = new Person(personDebt.getPerson());
                Debt debt = new Debt(personDebt.getDebt());

                PersonDebt newPersonDebt = new PersonDebt(person, debt);

                mCacheOweMePersonDebts.put(personDebt.getDebt().getId(), newPersonDebt);
            }
            mCacheOweMePersonDebtsIsDirty = false;
        }else if (debtType == Debt.DEBT_TYPE_IOWE) {
            if (mCacheIOwePersonDebts == null) {
                mCacheIOwePersonDebts = new LinkedHashMap<>();
            }
            mCacheIOwePersonDebts.clear();

            for (PersonDebt personDebt : personDebts) {
                Person person = new Person(personDebt.getPerson());
                Debt debt = new Debt(personDebt.getDebt());

                PersonDebt newPersonDebt = new PersonDebt(person, debt);
                mCacheIOwePersonDebts.put(personDebt.getDebt().getId(), newPersonDebt);
            }
            mCacheIOwePersonDebtsIsDirty = false;
        }
    }

    /**
     * Saves a {@link Debt} and {@link Person} to the loacl database and cache
     * @param debt the Debt object to be saved
     * @param person the Person object to be saved
     */
    @Override
    public void savePersonDebt(@NonNull Debt debt, @NonNull Person person) {
        checkNotNull(debt);
        checkNotNull(person);

        mDebtsLocalDataSource.savePersonDebt(debt, person);

        // Do in memory cache update to keep the app UI up to date
        Person cachePerson = getPerson(person.getPhoneNumber());
        if (cachePerson == null) {
            person.addDebt(debt);
            if (mCachePersons == null) {
                mCachePersons = new LinkedHashMap<>();
            }
            mCachePersons.put(person.getPhoneNumber(), person);
        }else {
            cachePerson.addDebt(debt);
        }

        if (debt.getDebtType() == Debt.DEBT_TYPE_OWED) {
            if (mCacheOweMePersonDebts == null) {
                mCacheOweMePersonDebts = new LinkedHashMap<>();
            }
            PersonDebt personDebt = new PersonDebt(person, debt);
            mCacheOweMePersonDebts.put(debt.getId(), personDebt);
        }else if (debt.getDebtType() == Debt.DEBT_TYPE_IOWE) {
            if (mCacheIOwePersonDebts == null) {
                mCacheIOwePersonDebts = new LinkedHashMap<>();
            }
            PersonDebt personDebt = new PersonDebt(person, debt);
            mCacheIOwePersonDebts.put(debt.getId(), personDebt);
        }
        //update the UI
       notifyContentObserver(debt.getDebtType());
    }

    @Override
    public void refreshDebts() {
        mCacheOweMePersonDebtsIsDirty = true;
        // notifyContentObserver();
    }

    /**
     * Delete all the {@link PersonDebt}s in local database and cache
     */
    @Override
    public void deleteAllPersonDebts() {

        mDebtsLocalDataSource.deleteAllPersonDebts();

        if (mCacheOweMePersonDebts == null) {
            mCacheOweMePersonDebts = new LinkedHashMap<>();
        }
        mCacheOweMePersonDebts.clear();

        if (mCacheIOwePersonDebts == null) {
            mCacheIOwePersonDebts = new LinkedHashMap<>();
        }
        mCacheIOwePersonDebts.clear();

        if (mCachePersons == null) {
            mCachePersons = new LinkedHashMap<>();
        }
        mCachePersons.clear();

        // update the UI
        // notifyContentObserver();
    }

    /**
     * Deletes a {@link PersonDebt} in local database and cache
     * @param personDebt
     */
    @Override
    public void deletePersonDebt(@NonNull PersonDebt personDebt) {
        checkNotNull(personDebt);
        mDebtsLocalDataSource.deletePersonDebt(personDebt);

        deletePersonDebtFromCache(personDebt);

        // Update the UI
        notifyContentObserver(personDebt.getDebt().getDebtType());
    }


    /**
     * Batch delete a List of {@link PersonDebt}s from cache and local database
     * @param personDebts the List of PersonDebt objects
     * @param debtType the debt type of the PersonDebt objects
     */
    @Override
    public void batchDelete(@NonNull List<PersonDebt> personDebts, @NonNull int debtType) {
        checkNotNull(personDebts);
        checkNotNull(debtType);

        for (PersonDebt personDebt : personDebts) {
            deletePersonDebtFromCache(personDebt);
        }

        mDebtsLocalDataSource.batchDelete(personDebts, debtType);

        // Update the UI
        notifyContentObserver(debtType);
    }

    /**
     * Returns all {@link Person}s with {@link Debt} from local database
     * @return a List of {@link Person}
     */
    @Override
    public List<Person> getAllPersonWithDebts() {
        List<Person> persons = null;

        if (!mCachePersonIsDirty) {
            if (mCachePersons == null) {
                persons = mDebtsLocalDataSource.getAllPersonWithDebts();
            } else {
                persons = new ArrayList<>();
                for (Person person : getCachePersons()) {
                    persons.add(person);
                }
                return persons;
            }
        }

        processLoadedPersons(persons);

        return persons;
    }

    /**
     * Saves the loaded {@link Person}s to cache
     * @param persons the loaded Person objects
     */
    private void processLoadedPersons(List<Person> persons) {

        if (persons == null) {
            mCachePersons = null;
            mCachePersonIsDirty = false;
            return;
        }

        if (mCachePersons == null) {
            mCachePersons = new LinkedHashMap<>();
        }
        mCachePersons.clear();

        for (Person person : persons) {
            mCachePersons.put(person.getPhoneNumber(), person);
        }
        mCachePersonIsDirty = false;
    }

    /**
     * Deletes a {@link PersonDebt} from cache
     * @param personDebt the PersonDebt object to be deleted
     */
    private void deletePersonDebtFromCache(PersonDebt personDebt) {

        // delete person if he/she has only one debt
        String personPhoneNumber = personDebt.getPerson().getPhoneNumber();
        if (personHasOneDebt(personPhoneNumber)) {
            // delete person from cache
            mCachePersons.remove(personPhoneNumber);
        }

        if (personDebt.getDebt().getDebtType() == Debt.DEBT_TYPE_OWED && !mCacheOweMePersonDebts.isEmpty()) {
            mCacheOweMePersonDebts.remove(personDebt.getDebt().getId());
        }else if (personDebt.getDebt().getDebtType() == Debt.DEBT_TYPE_IOWE && !mCacheIOwePersonDebts.isEmpty()) {
            mCacheIOwePersonDebts.remove(personDebt.getDebt().getId());
        }
    }

    /**
     * Checks if Person has one Debt
     * @param personPhoneNumber is the person id
     * @return either true or false
     */
    private boolean personHasOneDebt(String personPhoneNumber) {
        checkNotNull(personPhoneNumber);
        return getPersonDebts(personPhoneNumber).size() == 1;
    }

    /**
     * Deletes all PersonDebt by type in local database and cache
     * @param debtType is the debt type
     */
    @Override
    public void deleteAllPersonDebtsByType(@NonNull int debtType) {

        checkNotNull(debtType);
        mDebtsLocalDataSource.deleteAllPersonDebtsByType(debtType);

        if (debtType == Debt.DEBT_TYPE_OWED && mCacheOweMePersonDebts != null) {
            mCacheOweMePersonDebts.clear();
        }else if (debtType == Debt.DEBT_TYPE_IOWE && mCacheIOwePersonDebts != null) {
            mCacheIOwePersonDebts.clear();
        }
    }

    /**
     * Updates a {@link PersonDebt} in local database and cache
     * @param personDebt the PersonDebt object to be updated
     */
    @Override
    public void updatePersonDebt(@NonNull PersonDebt personDebt) {
        checkNotNull(personDebt);
        mDebtsLocalDataSource.updatePersonDebt(personDebt);
        updatePersonDebtCache(personDebt);
        updatePersonCacheDebts(personDebt);

        //update the UI
        notifyContentObserver(personDebt.getDebt().getDebtType());
    }

    /**
     * Updates a {@link Person} in Persons cache
     * @param personDebt the PersonDebt to be updated
     */
    private void updatePersonCacheDebts(@NonNull PersonDebt personDebt) {
        Person personCache = mCachePersons.get(personDebt.getPerson().getPhoneNumber());
        Debt foundDebt = null;
        for (Debt debt : personCache.getDebts()) {
            if (debt.getId().equals(personDebt.getDebt().getId())) {
                foundDebt = debt;
                break;
            }
        }

        int index = personCache.getDebts().indexOf(foundDebt);
        personCache.getDebts().set(index, personDebt.getDebt());
    }

    /**
     * Updates a {@link PersonDebt} in cache
     * @param personDebt the PersonDebt to be updated
     */
    private void updatePersonDebtCache(@NonNull PersonDebt personDebt) {
        if (personDebt.getDebt().getDebtType() == Debt.DEBT_TYPE_OWED) {
            mCacheOweMePersonDebts.get(personDebt.getDebt().getId()).setDebt(personDebt.getDebt());
            mCacheOweMePersonDebts.get(personDebt.getDebt().getId()).setPerson(personDebt.getPerson());

        }else if (personDebt.getDebt().getDebtType() == Debt.DEBT_TYPE_IOWE) {
            mCacheIOwePersonDebts.get(personDebt.getDebt().getId()).setDebt(personDebt.getDebt());
            mCacheIOwePersonDebts.get(personDebt.getDebt().getId()).setPerson(personDebt.getPerson());
        }
    }

    public interface DebtsRepositoryObserver {
        void onDebtsChanged(int debtType);
    }
}
