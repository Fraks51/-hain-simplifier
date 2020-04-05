package simplifier.chain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Chain {
    private List<Call> calls;

    /**
     * Chain's constructor
     */
    public Chain() {
        calls = new ArrayList<>();
    }

    /**
     * Chain's constructor with calls
     *
     * @param calls current calls for this chain
     */
    public Chain(List<Call> calls) {
        this.calls = calls;
    }

    /**
     * Add call to chain
     *
     * @param call target to add
     */
    public void add(Call call) {
        calls.add(call);
    }


    /**
     * Get calls in this chain
     *
     * @return {@link List} of calls
     */
    public List<Call> getCalls() {
        return calls;
    }

    @Override
    public String toString() {
        return calls.stream().map(Call::toString).collect(Collectors.joining("%>%"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chain chain = (Chain) o;
        return calls.equals(chain.calls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calls);
    }
}
