package app.mawared.alhayat.helpers;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class FragmentStack {

    private Activity activity;
    private FragmentManager manager;
    private int containerId;

    public FragmentStack(Activity activity, FragmentManager manager, int containerId) {
        this.activity = activity;
        this.manager = manager;
        this.containerId = containerId;
    }

    /**
     * Returns the number of fragments in the stack.
     *
     * @return the number of fragments in the stack.
     */
    public int size() {
        return getFragments().size();
    }

    /**
     * Pushes a fragment to the top of the stack. keeping the other fragments in the stack
     */
    public void push(Fragment fragment) {
        Fragment top = peek();
        if (top != null) {
            manager.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out)
                    .remove(top)
                    .add(containerId, fragment, indexToTag(manager.getBackStackEntryCount() + 1))
                    .addToBackStack(null)
                    .commit();
        } else {
            manager.beginTransaction()
                    .add(containerId, fragment, indexToTag(0))
                    .commit();
        }

        manager.executePendingTransactions();
    }

    /**
     * Pops the top item if the stack.
     * If the fragment implements {@link OnBackPressedHandlingFragment}, calls {@link OnBackPressedHandlingFragment#onBackPressed()} instead.
     * If {@link OnBackPressedHandlingFragment#onBackPressed()} returns false the fragment gets popped.
     *
     * @return true if a fragment has been popped or if {@link OnBackPressedHandlingFragment#onBackPressed()} returned true;
     */
    public boolean back() {
        Fragment top = peek();
        if (top instanceof OnBackPressedHandlingFragment) {
            if (((OnBackPressedHandlingFragment) top).onBackPressed())
                return true;
        }
        return pop();
    }

    /**
     * Pops the topmost fragment from the stack.
     * The lowest fragment can't be popped, it can only be replaced.
     *
     * @return false if the stack can't pop or true if a top fragment has been popped.
     */
    public boolean pop() {
        if (manager.getBackStackEntryCount() == 0)
            return false;
        manager.popBackStack();
        return true;
    }

    public void showFragment(Fragment fragment) {
        List<Fragment> fragments = getFragments();
        for (Fragment f : fragments) {
            if (f != null) {
                manager.beginTransaction().hide(f);
            }
        }
        manager.beginTransaction().show(fragment);
    }

    /**
     * Replaces stack contents with just one fragment and remove the others.
     *
     * @param fragment
     */
    public void replace(Fragment fragment) {
        manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        manager.beginTransaction()
                .replace(containerId, fragment, indexToTag(0))
                .commit();
        manager.executePendingTransactions();
    }

    /**
     * Returns the topmost fragment in the stack.
     */
    public Fragment peek() {
        return manager.findFragmentById(containerId);
    }

    /**
     * Returns a back fragment if the fragment is of given class.
     * If such fragment does not exist and activity implements the given class then the activity will be returned.
     *
     * @param fragment     a fragment to search from.
     * @param callbackType a class of type for callback to search.
     * @param <T>          a type of callback.
     * @return a back fragment or activity.
     */
    @SuppressWarnings("unchecked")
    public <T> T findCallback(Fragment fragment, Class<T> callbackType) {

        Fragment back = getBackFragment(fragment);

        if (back != null && callbackType.isAssignableFrom(back.getClass()))
            return (T) back;

        if (callbackType.isAssignableFrom(activity.getClass()))
            return (T) activity;

        return null;
    }

    public void removeFragment(Fragment fragment) {
        int i = 0;
        List<Fragment> fragments = getFragments();
        for (Fragment f : fragments) {
            if (f.equals(fragment)) {
                i++;
            }
        }
        if (i > 1) {
            FragmentManager manager = this.manager;
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove(fragment);
            trans.commit();
            manager.popBackStack();
        }
    }

    public Fragment getBackFragment(Fragment fragment) {
        List<Fragment> fragments = getFragments();
        for (int f = fragments.size() - 1; f >= 0; f--) {
            if (fragments.get(f) == fragment && f > 0)
                return fragments.get(f - 1);
        }
        return null;
    }

    public void restartFragment(Fragment fragment) {

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.detach(fragment).attach(fragment);
    }

    public List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>(manager.getBackStackEntryCount() + 1);
        for (int i = 0; i < manager.getBackStackEntryCount() + 1; i++) {
            Fragment fragment = manager.findFragmentByTag(indexToTag(i));
            if (fragment != null)
                fragments.add(fragment);
        }
        return fragments;
    }

    private String indexToTag(int index) {
        return Integer.toString(index);
    }

    public interface OnBackPressedHandlingFragment {
        boolean onBackPressed();
    }
}